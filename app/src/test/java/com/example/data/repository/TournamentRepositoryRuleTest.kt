package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class TournamentRepositoryRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun seedAndObserveTeams_authenticatedUser_emitsTeams(): Unit = runBlocking {
    val email = "admin_${UUID.randomUUID()}@test.com"
    val uid = signInTestUser(email)
    val repository = TournamentRepository(firestore)

    withTimeout(5000L) {
      repository.seedInitialDataIfEmpty(uid)
    }

    val teams = withTimeout(5000L) {
      repository.observeTeams(uid).first { it.isNotEmpty() }
    }

    assertEquals(12, teams.size)
    assertTrue(teams.first().name.isNotEmpty())
  }

  @Test
  fun updateTeam_authenticatedUser_persistsChanges(): Unit = runBlocking {
    val email = "admin_${UUID.randomUUID()}@test.com"
    val uid = signInTestUser(email)
    val repository = TournamentRepository(firestore)

    withTimeout(5000L) {
      repository.seedInitialDataIfEmpty(uid)
    }

    val initialTeams = withTimeout(5000L) {
      repository.observeTeams(uid).first { it.isNotEmpty() }
    }
    val teamToUpdate = initialTeams.first()
    val updatedTeam = teamToUpdate.copy(pts = 3, g = 1, pj = 1, gf = 2, gc = 0, dg = 2)

    withTimeout(5000L) {
      repository.updateTeam(uid, updatedTeam)
    }

    val updatedTeams = withTimeout(5000L) {
      repository.observeTeams(uid).first { list -> list.any { it.id == teamToUpdate.id && it.pts == 3 } }
    }
    assertEquals(3, updatedTeams.first { it.id == teamToUpdate.id }.pts)
  }

  @Test
  fun observeTeams_unauthenticatedUser_fails(): Unit = runBlocking {
    auth.signOut()
    val repository = TournamentRepository(firestore)

    var failed = false
    try {
      repository.observeTeams("some_user_id").first()
    } catch (e: IllegalStateException) {
      failed = true
    }
    assertTrue("Should throw IllegalStateException when unauthenticated", failed)
  }
}

package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class Match(
  @DocumentId val id: String = "",
  val round: Int = 1,
  val homeTeamId: String = "",
  val homeTeamName: String = "",
  val awayTeamId: String = "",
  val awayTeamName: String = "",
  val homeScore: Int = 0,
  val awayScore: Int = 0,
  val status: String = "scheduled", // scheduled, live, finished
  val matchDate: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null
)

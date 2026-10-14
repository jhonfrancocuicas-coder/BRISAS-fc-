package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class Team(
  @DocumentId val id: String = "",
  val name: String = "",
  val shortName: String = "",
  val shieldColor: String = "#1B4D3E",
  val accentColor: String = "#FFD700",
  val badgeIcon: String = "shield",
  val position: Int = 1,
  val pj: Int = 0,
  val g: Int = 0,
  val e: Int = 0,
  val p: Int = 0,
  val gf: Int = 0,
  val gc: Int = 0,
  val dg: Int = 0,
  val pts: Int = 0,
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null
)

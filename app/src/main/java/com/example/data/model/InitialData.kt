package com.example.data.model

object InitialData {
  const val TOURNAMENT_NAME = "Liga Fronteo Obelisco"
  const val TOURNAMENT_SUBTITLE = "Liga Inicial 2026 - Temporada 2026"

  fun getDefaultTeams(): List<Team> = listOf(
    Team(
      id = "team_alpha",
      name = "ALPHA",
      shortName = "ALP",
      shieldColor = "#581C87",
      accentColor = "#C084FC",
      badgeIcon = "wolf",
      position = 1,
      pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0
    ),
    Team(
      id = "team_caimanes",
      name = "CAIMANES FC",
      shortName = "CAI",
      shieldColor = "#14532D",
      accentColor = "#4ADE80",
      badgeIcon = "alligator",
      position = 2,
      pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0
    ),
    Team(
      id = "team_caruciena",
      name = "CARUCIENA SC",
      shortName = "CSC",
      shieldColor = "#78350F",
      accentColor = "#FDE047",
      badgeIcon = "circle_shield",
      position = 3,
      pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0
    ),
    Team(
      id = "team_crisban",
      name = "CRISBAN FC",
      shortName = "CRI",
      shieldColor = "#1E3A8A",
      accentColor = "#60A5FA",
      badgeIcon = "star_shield",
      position = 4,
      pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0
    ),
    Team(
      id = "team_deportivo_sin_luz",
      name = "DEPORTIVO SIN LUZ",
      shortName = "DSL",
      shieldColor = "#9A3412",
      accentColor = "#FB923C",
      badgeIcon = "castle_shield",
      position = 5,
      pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0
    ),
    Team(
      id = "team_elite",
      name = "ELITE F.C.",
      shortName = "ELI",
      shieldColor = "#854D0E",
      accentColor = "#FACC15",
      badgeIcon = "crown_shield",
      position = 6,
      pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0
    ),
    Team(
      id = "team_familis",
      name = "FAMILIS FC",
      shortName = "FAM",
      shieldColor = "#065F46",
      accentColor = "#34D399",
      badgeIcon = "beast_shield",
      position = 7,
      pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0
    ),
    Team(
      id = "team_halcones",
      name = "HALCONES FC",
      shortName = "HAL",
      shieldColor = "#991B1B",
      accentColor = "#F87171",
      badgeIcon = "falcon_shield",
      position = 8,
      pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0
    ),
    Team(
      id = "team_leones",
      name = "LEONES FC",
      shortName = "LEO",
      shieldColor = "#1D4ED8",
      accentColor = "#93C5FD",
      badgeIcon = "lion_shield",
      position = 9,
      pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0
    ),
    Team(
      id = "team_los_chamos",
      name = "LOS CHAMOS FC",
      shortName = "CHA",
      shieldColor = "#0F766E",
      accentColor = "#2DD4BF",
      badgeIcon = "badge_shield",
      position = 10,
      pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0
    ),
    Team(
      id = "team_new_fc",
      name = "NEW FC",
      shortName = "NEW",
      shieldColor = "#881337",
      accentColor = "#FB7185",
      badgeIcon = "star_circle",
      position = 11,
      pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0
    ),
    Team(
      id = "team_sporting_visaje",
      name = "SPORTING VISAJE",
      shortName = "VIS",
      shieldColor = "#0369A1",
      accentColor = "#38BDF8",
      badgeIcon = "wings_shield",
      position = 12,
      pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0
    )
  )

  fun getDefaultMatches(): List<Match> = listOf(
    // Jornada 1
    Match(
      id = "match_j1_1",
      round = 1,
      homeTeamId = "team_alpha",
      homeTeamName = "ALPHA",
      awayTeamId = "team_caimanes",
      awayTeamName = "CAIMANES FC",
      homeScore = 0,
      awayScore = 0,
      status = "scheduled",
      matchDate = "Sábado - 14:00"
    ),
    Match(
      id = "match_j1_2",
      round = 1,
      homeTeamId = "team_caruciena",
      homeTeamName = "CARUCIENA SC",
      awayTeamId = "team_crisban",
      awayTeamName = "CRISBAN FC",
      homeScore = 0,
      awayScore = 0,
      status = "scheduled",
      matchDate = "Sábado - 15:30"
    ),
    Match(
      id = "match_j1_3",
      round = 1,
      homeTeamId = "team_deportivo_sin_luz",
      homeTeamName = "DEPORTIVO SIN LUZ",
      awayTeamId = "team_elite",
      awayTeamName = "ELITE F.C.",
      homeScore = 0,
      awayScore = 0,
      status = "scheduled",
      matchDate = "Sábado - 17:00"
    ),
    Match(
      id = "match_j1_4",
      round = 1,
      homeTeamId = "team_familis",
      homeTeamName = "FAMILIS FC",
      awayTeamId = "team_halcones",
      awayTeamName = "HALCONES FC",
      homeScore = 0,
      awayScore = 0,
      status = "scheduled",
      matchDate = "Domingo - 14:00"
    ),
    Match(
      id = "match_j1_5",
      round = 1,
      homeTeamId = "team_leones",
      homeTeamName = "LEONES FC",
      awayTeamId = "team_los_chamos",
      awayTeamName = "LOS CHAMOS FC",
      homeScore = 0,
      awayScore = 0,
      status = "scheduled",
      matchDate = "Domingo - 15:30"
    ),
    Match(
      id = "match_j1_6",
      round = 1,
      homeTeamId = "team_new_fc",
      homeTeamName = "NEW FC",
      awayTeamId = "team_sporting_visaje",
      awayTeamName = "SPORTING VISAJE",
      homeScore = 0,
      awayScore = 0,
      status = "scheduled",
      matchDate = "Domingo - 17:00"
    ),
    // Jornada 2
    Match(
      id = "match_j2_1",
      round = 2,
      homeTeamId = "team_caimanes",
      homeTeamName = "CAIMANES FC",
      awayTeamId = "team_caruciena",
      awayTeamName = "CARUCIENA SC",
      homeScore = 0,
      awayScore = 0,
      status = "scheduled",
      matchDate = "Siguiente Semana"
    ),
    Match(
      id = "match_j2_2",
      round = 2,
      homeTeamId = "team_crisban",
      homeTeamName = "CRISBAN FC",
      awayTeamId = "team_deportivo_sin_luz",
      awayTeamName = "DEPORTIVO SIN LUZ",
      homeScore = 0,
      awayScore = 0,
      status = "scheduled",
      matchDate = "Siguiente Semana"
    ),
    Match(
      id = "match_j2_3",
      round = 2,
      homeTeamId = "team_elite",
      homeTeamName = "ELITE F.C.",
      awayTeamId = "team_familis",
      awayTeamName = "FAMILIS FC",
      homeScore = 0,
      awayScore = 0,
      status = "scheduled",
      matchDate = "Siguiente Semana"
    ),
    Match(
      id = "match_j2_4",
      round = 2,
      homeTeamId = "team_halcones",
      homeTeamName = "HALCONES FC",
      awayTeamId = "team_leones",
      awayTeamName = "LEONES FC",
      homeScore = 0,
      awayScore = 0,
      status = "scheduled",
      matchDate = "Siguiente Semana"
    ),
    Match(
      id = "match_j2_5",
      round = 2,
      homeTeamId = "team_los_chamos",
      homeTeamName = "LOS CHAMOS FC",
      awayTeamId = "team_new_fc",
      awayTeamName = "NEW FC",
      homeScore = 0,
      awayScore = 0,
      status = "scheduled",
      matchDate = "Siguiente Semana"
    ),
    Match(
      id = "match_j2_6",
      round = 2,
      homeTeamId = "team_sporting_visaje",
      homeTeamName = "SPORTING VISAJE",
      awayTeamId = "team_alpha",
      awayTeamName = "ALPHA",
      homeScore = 0,
      awayScore = 0,
      status = "scheduled",
      matchDate = "Siguiente Semana"
    )
  )
}

package com.example.f1widgetapp.data.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.f1widgetapp.data.modals.Race

@Dao
interface RaceDao {
    @Upsert
    suspend fun upsertRaces(races: List<Race>)

    @Query("SELECT * FROM races WHERE season = :season ORDER BY round ASC")
    suspend fun getRacesForSeason(season: String): List<Race>

    @Query("SELECT * FROM races WHERE season = :season AND round = :round")
    suspend fun getRaceByRound(season: String, round: String): Race?

    @Query("DELETE FROM races WHERE season < :season")
    suspend fun deleteOldSeasons(season: String)
}

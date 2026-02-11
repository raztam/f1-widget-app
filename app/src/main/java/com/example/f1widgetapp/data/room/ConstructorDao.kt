package com.example.f1widgetapp.data.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.f1widgetapp.data.modals.Constructor

@Dao
interface ConstructorDao {
    @Upsert
    suspend fun upsertConstructors(constructors: List<Constructor>)

    @Query("SELECT * FROM constructors")
    suspend fun getAllConstructors(): List<Constructor>

    @Query("SELECT * FROM constructors WHERE constructorId = :id")
    suspend fun getConstructorById(id: String): Constructor?

    @Query("UPDATE constructors SET position = :position, score = :points WHERE constructorId = :constructorId")
    suspend fun updateConstructorStandings(constructorId: String, position: String, points: String)
}

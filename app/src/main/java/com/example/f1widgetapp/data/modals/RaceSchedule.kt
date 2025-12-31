package com.example.f1widgetapp.data.modals

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class RaceScheduleResponse(
    @SerializedName("MRData")
    val mrData: RaceMRData
)

data class RaceMRData(
    val xmlns: String,
    val series: String,
    @SerializedName("RaceTable")
    val raceTable: RaceTable
)

data class RaceTable(
    val season: String,
    @SerializedName("Races")
    val races: List<RaceResponse>
)

data class RaceResponse(
    val season: String,
    val round: String,
    val raceName: String,
    val date: String,
    val time: String,
    @SerializedName("Circuit")
    val circuit: Circuit? = null,
    @SerializedName("Sprint")
    val sprint: SessionTime? = null
)

data class Circuit(
    val circuitName: String
)

data class SessionTime(
    val date: String,
    val time: String
)

@Entity(tableName = "races")
data class Race(
    @PrimaryKey
    val round: String,
    val season: String,
    val raceName: String,
    val date: String,  // Race date: "2025-11-09"
    val time: String,  // Race time: "17:00:00Z"
    val circuitName: String?,
    val sprintDate: String? = null,  // Sprint date: "2025-11-08" (if exists)
    val sprintTime: String? = null   // Sprint time: "14:00:00Z" (if exists)
) {
    /**
     * Calculate the end time of the main race (race start + 2 hours)
     */
    fun getRaceEndTimeMillis(): Long {
        return calculateEndTime(date, time, durationHours = 2)
    }

    /**
     * Calculate the end time of the sprint race (sprint start + 1 hour)
     * Returns null if this race doesn't have a sprint
     */
    fun getSprintEndTimeMillis(): Long? {
        if (sprintDate == null || sprintTime == null) return null
        return calculateEndTime(sprintDate, sprintTime, durationHours = 1)
    }

    /**
     * Check if this race weekend has a sprint race
     */
    fun hasSprint(): Boolean = sprintDate != null && sprintTime != null

    /**
     * Helper function to calculate event end time
     */
    private fun calculateEndTime(date: String, time: String, durationHours: Int): Long {
        val dateTime = "${date}T${time}"
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val startTime = formatter.parse(dateTime)?.time ?: 0L
        return startTime + (durationHours * 60 * 60 * 1000)
    }
}

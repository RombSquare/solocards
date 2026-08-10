package com.rombsquare.solocards.data.room.converters

import androidx.room.TypeConverter
import com.rombsquare.solocards.domain.models.Satisfaction
import kotlin.time.Instant

class SatisfactionConverter {
    @TypeConverter
    fun satisfactionToInt(satis: Satisfaction): Int {
        return Satisfaction.entries.indexOf(satis)
    }

    @TypeConverter
    fun intToSatisfaction(int: Int): Satisfaction {
        return Satisfaction.entries[int]
    }
}
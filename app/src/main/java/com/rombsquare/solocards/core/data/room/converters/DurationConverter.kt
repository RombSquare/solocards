package com.rombsquare.solocards.core.data.room.converters

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class DurationConverter {
    @TypeConverter
    fun intToDuration(value: Int): Duration {
        return value.seconds
    }

    @TypeConverter
    fun durationToInt(duration: Duration): Int {
        return duration.inWholeSeconds.toInt()
    }
}
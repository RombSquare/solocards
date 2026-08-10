package com.rombsquare.solocards.domain.utils

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Duration
import kotlin.time.Instant
import java.time.Instant as JavaInstant

fun Instant.toDateFormat(): String {
    val javaInstant = JavaInstant.ofEpochSecond(this.epochSeconds, this.nanosecondsOfSecond.toLong())

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        .withZone(ZoneId.systemDefault())

    return formatter.format(javaInstant)
}

fun Duration.toMinuteSecondFormat(): String {
    return "${this.inWholeMinutes}m ${this.inWholeSeconds % 60}s"
}

fun Duration.toColonFormat(): String {
    var minutesString = this.inWholeMinutes.toString()
    var secondsString = (this.inWholeSeconds % 60).toString()

    if (minutesString.length == 1) minutesString = "0$minutesString"
    if (secondsString.length == 1) secondsString = "0$secondsString"



    return "$minutesString:$secondsString"
}
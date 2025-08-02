package com.example.mathapp.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long?.changeMillisToDateString(): String {
    val date: LocalDate = this?.let {
        Instant
            .ofEpochMilli(it) //Converts the Long (milliseconds since 1970) into an Instant (a specific point in time in UTC)
            .atZone(ZoneId.systemDefault())
            .toLocalDate() //Drops the time part and keeps just the date
    } ?: LocalDate.now() // this whole block Convert millis into a local calendar date (like 27/07/2025)
    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

fun Long.toHours(): Float {
    val hours = this.toFloat() / 3600f
    return "%.2f".format(hours).toFloat()
}
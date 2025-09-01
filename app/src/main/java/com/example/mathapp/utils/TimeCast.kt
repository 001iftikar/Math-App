package com.example.mathapp.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun Long?.changeMillisToDateString(): String {
    val date: LocalDate = this?.let {
        java.time.Instant
            .ofEpochMilli(it) //Converts the Long (milliseconds since 1970) into an Instant (a specific point in time in UTC)
            .atZone(ZoneId.systemDefault())
            .toLocalDate() //Drops the time part and keeps just the date
    }
        ?: LocalDate.now() // this whole block Convert millis into a local calendar date (like 27/07/2025)
    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}


fun Long.toHours(): Float {
    val hours = this.toFloat() / 3600f
    return "%.2f".format(hours).toFloat()
}
@OptIn(ExperimentalTime::class)
object SupabaseTimeCast {

    // TO STORE THE TIME IN THE SUPABASE TIMESTAMPZ FORMAT
    fun Long?.toTimestampZ(): String {
        return Instant
            .fromEpochMilliseconds(this ?: System.currentTimeMillis())
            .toString()
    }



    // TO DISPLAY THE SUPABASE FORMATTED DATE TO USER READABLE FORMAT
    fun String.formattedTimestampZ(): String {
        val instant = Instant.parse(this)
        val localDate = instant.toLocalDateTime(
            TimeZone.currentSystemDefault()
        ).date

        val formatter = kotlinx.datetime.LocalDate.Format {
            day(padding = Padding.ZERO)
            char(' ')
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            year()
        }

        return formatter.format(localDate)
    }

    fun String.toEpochMillisFromFormatted(): Long {
        // Define the formatter for "22 Aug 2025"
        val formatter = kotlinx.datetime.LocalDate.Format {
            day(padding = Padding.ZERO)
            char(' ')
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            year()
        }

        // Parse the string to LocalDate
        val localDate = formatter.parse(this)

        // Convert LocalDate to LocalDateTime (midnight)
        val localDateTime = localDate.atStartOfDayIn(TimeZone.currentSystemDefault())

        // Return as epoch millis
        return localDateTime.toEpochMilliseconds()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
object SelectableDatesImpl : SelectableDates {

    private val timeZone = TimeZone.currentSystemDefault()
    @OptIn(ExperimentalTime::class)
    private val today = Clock.System.now().toLocalDateTime(timeZone).date
    @OptIn(ExperimentalTime::class)
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val date = Instant.fromEpochMilliseconds(utcTimeMillis)
            .toLocalDateTime(timeZone).date

        return date > today
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= today.year
    }
}
package com.oliver.weatherapp.utils

import android.content.Context
import com.oliver.weatherapp.R
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val fullDate = SimpleDateFormat(", MMM dd, HH:mm", Locale.US)
    private val dayFormat = SimpleDateFormat("EEEE", Locale.US)

    fun getFriendlyDateString(context: Context, dateInMillis: Long): String {
        val today = Calendar.getInstance()
        val date = Calendar.getInstance()
        date.time = Date(dateInMillis)
        val dayName: String
        dayName = when {
            date.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH) -> context.getString(R.string.today)
            date.get(Calendar.DAY_OF_MONTH) - today.get(Calendar.DAY_OF_MONTH) == 1 -> context.getString(R.string.tomorrow)
            else -> dayFormat.format(date.time)
        }

        return dayName + fullDate.format(Date(dateInMillis))
    }
}

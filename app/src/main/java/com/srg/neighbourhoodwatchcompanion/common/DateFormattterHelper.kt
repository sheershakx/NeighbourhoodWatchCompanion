package com.srg.neighbourhoodwatchcompanion.common

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
fun Long?.formatDateFromMillis(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
    return sdf.format(Date(this ?: 0L))
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.formatDateTimeForDisplay(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | h:mm a", Locale.getDefault())
    val dateTime = LocalDateTime.parse(this, inputFormatter)
    return dateTime.format(outputFormatter)
}

//@SuppressLint("SimpleDateFormat")
//@RequiresApi(Build.VERSION_CODES.O)
//fun test(date: String, time: String) {
//    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
//    sdf.format()
//}
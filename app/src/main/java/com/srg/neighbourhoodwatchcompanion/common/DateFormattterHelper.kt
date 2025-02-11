package com.srg.neighbourhoodwatchcompanion.common

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
fun Long?.formatDateFromMillis(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
    return sdf.format(Date(this ?: 0L))
}


//@SuppressLint("SimpleDateFormat")
//@RequiresApi(Build.VERSION_CODES.O)
//fun test(date: String, time: String) {
//    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
//    sdf.format()
//}
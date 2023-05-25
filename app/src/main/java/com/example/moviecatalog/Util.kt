package com.example.moviecatalog

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Objects
import java.util.TimeZone

fun isValidEmail(value: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(value).matches()
}

fun convertServerDateToUserTimeZone(serverDate: String?): String {
    var ourdate: String
    try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val value = serverDate?.let { formatter.parse(it) }
        val timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US) //this format changeable
        dateFormatter.timeZone = timeZone
        ourdate = dateFormatter.format(Objects.requireNonNull(value))

    } catch (e: Exception) {
        e.printStackTrace()
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = serverDate?.let { formatter.parse(it) }
            val timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US) //this format changeable
            dateFormatter.timeZone = timeZone
            ourdate = dateFormatter.format(Objects.requireNonNull(value))

        } catch (e: Exception) {
            e.printStackTrace()
            ourdate = "0000-00-00 00:00:00"
        }
    }
    return ourdate
}

fun convertServerDateToUserTimeZone2(serverDate: String?): String {
    var ourdate: String
    try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val value = serverDate?.let { formatter.parse(it) }
        val timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US) //this format changeable
        dateFormatter.timeZone = timeZone
        ourdate = dateFormatter.format(Objects.requireNonNull(value))

    } catch (e: Exception) {
        e.printStackTrace()
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = serverDate?.let { formatter.parse(it) }
            val timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US) //this format changeable
            dateFormatter.timeZone = timeZone
            ourdate = dateFormatter.format(Objects.requireNonNull(value))

        } catch (e: Exception) {
            e.printStackTrace()
            ourdate = "0000-00-00 00:00:00"
        }
    }
    return ourdate
}

fun convertServerDate(serverDate: String?): String {
    var ourdate: String
    try {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val value = serverDate?.let { formatter.parse(it) }
        val timeZone = TimeZone.getTimeZone("UTC")
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US) //this format changeable
        dateFormatter.timeZone = timeZone
        ourdate = dateFormatter.format(Objects.requireNonNull(value))

    } catch (e: Exception) {
        e.printStackTrace()
        ourdate = "0000-00-00T00:00:00"
    }
    return ourdate
}
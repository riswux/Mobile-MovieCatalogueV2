package com.example.moviecatalog

import android.content.Context

class Preferences(val context: Context) {
    val preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        preferences.edit().putString("USER_TOKEN", token).apply()
    }

    fun getToken(): String {
        return preferences.getString("USER_TOKEN", "") ?: ""
    }

    fun deleteToken() {
        preferences.edit().clear().apply()
    }
}
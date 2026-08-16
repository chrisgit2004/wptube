package com.example.metrotube.data

import android.content.Context

/**
 * Thin wrapper around SharedPreferences for the one setting this app has.
 * The key is stored in plain SharedPreferences for simplicity — if you plan
 * to distribute this build, switch to EncryptedSharedPreferences
 * (androidx.security:security-crypto) instead.
 */
object Prefs {
    private const val FILE = "metrotube_prefs"
    private const val KEY_API_KEY = "api_key"

    fun getApiKey(context: Context): String? {
        val prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        return prefs.getString(KEY_API_KEY, null)?.takeIf { it.isNotBlank() }
    }

    fun setApiKey(context: Context, key: String) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_API_KEY, key.trim())
            .apply()
    }

    fun hasApiKey(context: Context): Boolean = !getApiKey(context).isNullOrBlank()
}

package com.chrisrich4982.metrotube.data

import android.content.Context

enum class AppTheme {
    CLASSIC, // 2013 / Windows Phone look
    MODERN   // today's YouTube-ish look
}

/**
 * Thin wrapper around SharedPreferences for the settings this app has.
 * The key is stored in plain SharedPreferences for simplicity — if you plan
 * to distribute this build, switch to EncryptedSharedPreferences
 * (androidx.security:security-crypto) instead.
 */
object Prefs {
    private const val FILE = "metrotube_prefs"
    private const val KEY_API_KEY = "api_key"
    private const val KEY_APP_THEME = "app_theme"

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

    fun getAppTheme(context: Context): AppTheme {
        val prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        val name = prefs.getString(KEY_APP_THEME, AppTheme.CLASSIC.name)
        return try {
            AppTheme.valueOf(name ?: AppTheme.CLASSIC.name)
        } catch (e: IllegalArgumentException) {
            AppTheme.CLASSIC
        }
    }

    fun setAppTheme(context: Context, theme: AppTheme) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_APP_THEME, theme.name)
            .apply()
    }
}

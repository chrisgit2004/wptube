package com.chrisrich4982.metrotube.ui

import android.content.Context
import android.content.Intent
import com.chrisrich4982.metrotube.data.Prefs

object HomeRouter {

    /** Launches the correct home activity for the current theme, clearing any back stack. */
    fun goHome(context: Context) {
        val destination = if (Prefs.getUiTheme(context) == Prefs.THEME_CLASSIC) {
            HubActivity::class.java
        } else {
            MainActivity::class.java
        }
        val intent = Intent(context, destination).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }
}

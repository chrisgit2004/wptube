package com.chrisrich4982.metrotube.ui

import android.content.Context
import android.content.Intent
import com.chrisrich4982.metrotube.data.AppTheme
import com.chrisrich4982.metrotube.data.Prefs

object HomeRouter {

    fun goHome(context: Context) {
        val destination = if (Prefs.getAppTheme(context) == AppTheme.CLASSIC) {
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

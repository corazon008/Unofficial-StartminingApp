package com.example.startmining.Widgets

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.content.edit
import com.example.startmining.Constants

object WidgetDataRepository {
    fun setValue(context: Context, key: WidgetDataKey, value: String) {
        val prefs = context.getSharedPreferences(Constants.PREFS_KEY_WIDGETS, Context.MODE_PRIVATE)
        prefs.edit {
            putString(key.toString(), value)
        }
        //notifyWidgetUpdate(context)
    }

    fun getValue(context: Context, key: WidgetDataKey, default: String = "-1"): String {
        val prefs = context.getSharedPreferences(Constants.PREFS_KEY_WIDGETS, Context.MODE_PRIVATE)
        return prefs.getString(key.toString(), default) ?: default
    }

    private fun notifyWidgetUpdate(context: Context) {
        val intent = Intent(context, Dashboard::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        val ids = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context, Dashboard::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }
}

enum class WidgetDataKey(string: String) {
    BALANCE("balance"),
    EARNINGS("earnings"),
}
package com.example.startmining.Widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.startmining.Datas
import com.example.startmining.R
import java.util.Calendar


class Dashboard : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        Datas.LoadWalletsAddress(context)

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.dashboard_widgets)

    val calendar = Calendar.getInstance()
    val hour24hrs = calendar[Calendar.HOUR_OF_DAY]
    val minutes = calendar[Calendar.MINUTE]

    /*val refresh_thread = Thread { Datas.RefreshStake() }

    refresh_thread.start()
    refresh_thread.join()

    Datas.RefreshTextValue()
    views.setTextViewText(R.id.widget_live_rewards, RoundBTC(Datas.live_rewards, 7))
    views.setTextViewText(R.id.widget_next_payout, DateNextPayout())
    views.setTextViewText(R.id.widget_earnings, RoundBTC(Datas.earnings, 7))
    views.setTextViewText(R.id.textView5, "${hour24hrs}:${minutes}")*/

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
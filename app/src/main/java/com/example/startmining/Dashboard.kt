package com.example.startmining

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews


/**
 * Implementation of App Widget functionality.
 */
class Dashboard : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        val sharedPref = context.getSharedPreferences(context.getString(R.string.file_name), Context.MODE_PRIVATE)

        Datas.btc_wallet = sharedPref.getString(context.getString(R.string.btc_address), "").toString()
        Datas.eth_wallet = sharedPref.getString(context.getString(R.string.eth_address), "").toString()

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

    companion object {
        var i = 1
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.dashboard)
    Datas.RefreshStake()
    Datas.RefreshTextValue()
    views.setTextViewText(R.id.widget_live_rewards, RoundBTC(Datas.live_rewards))
    views.setTextViewText(R.id.widget_next_payout, NextPayout())
    views.setTextViewText(R.id.widget_earnings, RoundBTC(Datas.earnings))
    views.setTextViewText(R.id.textView5, Dashboard.i.toString())
    Dashboard.i++

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
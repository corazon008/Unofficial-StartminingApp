package com.example.startmining.Widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.startmining.Datas
import com.example.startmining.DateNextPayout
import com.example.startmining.R
import com.example.startmining.RoundBTC
import com.example.startmining.SessionManager
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
    SessionManager.updateBalance(Datas.btc_wallet, context)
    SessionManager.updatePoolsInfo(Datas.eth_wallet, context)
    // Wait for 5 seconds to ensure data is loaded
    Thread.sleep(5000)
    val views = RemoteViews(context.packageName, R.layout.dashboard_widget)

    val balance = WidgetDataRepository.getValue(context, WidgetDataKey.BALANCE).toDouble()
    val earnings = WidgetDataRepository.getValue(context, WidgetDataKey.EARNINGS).toDouble()

    views.setTextViewText(R.id.widget_live_rewards, RoundBTC(balance, 6))
    views.setTextViewText(R.id.widget_earnings, RoundBTC(earnings))
    views.setTextViewText(R.id.widget_next_payout, DateNextPayout(balance, earnings))

    val calendar = Calendar.getInstance()
    val hour24 = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    views.setTextViewText(R.id.sync_date, "$hour24:$minute")

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

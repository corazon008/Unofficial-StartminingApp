package com.example.startmining.Widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import com.example.startmining.Datas
import com.example.startmining.DateNextPayout
import com.example.startmining.R
import com.example.startmining.RoundBTC
import com.example.startmining.network.cruxpool.CruxpoolService
import com.example.startmining.network.cruxpool.balance.BalanceWrapper
import com.example.startmining.network.pools.PoolsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.pow


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
    val views = RemoteViews(context.packageName, R.layout.dashboard_widget)
    val calendar = Calendar.getInstance()
    val hour24 = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    views.setTextViewText(R.id.sync_date, "$hour24:$minute")

    var balance: Double? = null
    var earnings: Double? = null

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val balanceWrapper: BalanceWrapper = CruxpoolService.getBalance(Datas.btc_wallet)
            balance = balanceWrapper.data.balance / 10.0.pow(8.0)
            Log.d("DashboardWidget", "Balance updated: $balance")
        } catch (e: Exception) {
            Log.e("DashboardWidget", "Error fetching balance: ${e.message}")
        }

        try {
            val poolsInfo = PoolsService.updatePoolsInfo(Datas.eth_wallet)
            earnings = poolsInfo.sumOf { it.userEarnings }
            Log.d("DashboardWidget", "Earnings updated: $earnings")
        } catch (e: Exception) {
            Log.e("DashboardWidget", "Error fetching earnings: ${e.message}")
        }

        // Once the data is fetched, update the UI on the main thread
        kotlinx.coroutines.withContext(Dispatchers.Main) {
            views.setTextViewText(R.id.widget_live_rewards, RoundBTC(balance!!, 6))
            views.setTextViewText(R.id.widget_earnings, RoundBTC(earnings!!))
            views.setTextViewText(R.id.widget_next_payout, DateNextPayout(balance!!, earnings!!))

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

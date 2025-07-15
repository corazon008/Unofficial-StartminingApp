package com.example.startmining

import android.content.Context

object Datas {
    var btc_wallet: String = ""
        private set

    var eth_wallet: String = ""
        private set

    fun LoadWalletsAddress(application: Context?) {
        val sharedPref =
            application?.getSharedPreferences(
                Constants.PREFS_NAME,
                Context.MODE_PRIVATE
            )
        this.btc_wallet = sharedPref!!.getString(
            application.getString(R.string.btc_address),
            "bc0000000000000000000000000000000000000000"
        ).toString()
        this.eth_wallet = sharedPref.getString(
            application.getString(R.string.eth_address),
            "0x0000000000000000000000000000000000000000"
        ).toString()
    }

    fun SaveWalletsAddress(application: Context?, btc_address: String, eth_address: String) {
        val sharedPref = application?.getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )
        if (sharedPref != null) {
            with(sharedPref.edit()) {
                putString(application.getString(R.string.btc_address), btc_address.toString())
                putString(application.getString(R.string.eth_address), eth_address.toString())
                commit()
            }
        }
    }
}

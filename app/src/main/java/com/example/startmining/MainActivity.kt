package com.example.startmining

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.startmining.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //make all the requests during the launch
        val sharedPref =
            application?.getSharedPreferences(getString(R.string.file_name), Context.MODE_PRIVATE)

        Datas.btc_wallet = sharedPref!!.getString(getString(R.string.btc_address), "").toString()
        Datas.eth_wallet = sharedPref.getString(getString(R.string.eth_address), "").toString()

        Datas.refresh_thread.start()
        Datas.get_btc_should_have_thread.start()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_pools,
                R.id.navigation_simulator,
                R.id.navigation_options
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}


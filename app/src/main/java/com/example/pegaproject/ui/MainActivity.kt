package com.example.pegaproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.pegaproject.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navController =
            supportFragmentManager.findFragmentById((R.id.nav_host_fragment)) as NavHostFragment

        NavigationUI.setupActionBarWithNavController(
            this,
            navController.navController,
        )
        toolbar.setNavigationOnClickListener {
            navController.navController.navigateUp()
        }
    }

    fun setBarTitle(name: String) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = name
    }
}
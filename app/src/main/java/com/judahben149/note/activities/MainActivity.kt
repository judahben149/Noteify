package com.judahben149.note.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.judahben149.note.R
import com.judahben149.note.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()
    }

    private fun setUpNavigation() {
        //initialize variables here
        navView = binding.navigationView
        drawerLayout = binding.drawerLayout
        setSupportActionBar(binding.toolBar)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navView.menu, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
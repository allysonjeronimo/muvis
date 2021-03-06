package com.allysonjeronimo.muvis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.allysonjeronimo.muvis.ui.movielist.MovieListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavController()
        setupToolbar()
        setupBottomNavigation()
    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.movieListFragment, R.id.searchFragment
        ).build()
    }

    private fun setupToolbar(){
        this.setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNavigation() {
        bottom_navigation.setupWithNavController(navController)
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            if(item.itemId == R.id.favoritesFragment){
                navController.navigate(
                    R.id.movieListFragment,
                    Bundle().apply {
                        putBoolean(MovieListFragment.PARAM_FAVORITES, true)
                    })
            }
            else{
                navController.navigate(item.itemId)
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
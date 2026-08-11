package com.eventogo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.eventogo.ui.clientes.ClientesFragment
import com.eventogo.ui.clientes.EventosFragment
import com.eventogo.ui.clientes.PagosFragment
import com.eventogo.ui.clientes.ProveedoresFragment
import com.eventogo.ui.home.HomeFragment
import com.eventogo.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_clientes -> replaceFragment(ClientesFragment())
                R.id.nav_eventos -> replaceFragment(EventosFragment())
                R.id.nav_proveedores -> replaceFragment(ProveedoresFragment())
                R.id.nav_pagos -> replaceFragment(PagosFragment())
                R.id.nav_logout -> logout()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            navView.setCheckedItem(R.id.nav_home)
        }
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
package com.eventogo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.eventogo.dao.UsuarioDAO
import com.eventogo.ui.clientes.ClientesFragment
import com.eventogo.ui.clientes.EventosFragment
import com.eventogo.ui.clientes.PagosFragment
import com.eventogo.ui.clientes.ProveedoresFragment
import com.eventogo.ui.home.HomeFragment
import com.eventogo.ui.home.PerfilFragment
import com.eventogo.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var usuarioDAO: UsuarioDAO
    private var idUsuarioLogueado: Int = -1

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val path = it.toString()
            usuarioDAO.actualizarFoto(idUsuarioLogueado, path)
            actualizarHeader()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usuarioDAO = UsuarioDAO(this)
        val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)
        idUsuarioLogueado = prefs.getInt("id_usuario", -1)

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

        actualizarHeader()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_perfil -> replaceFragment(PerfilFragment())
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

    fun actualizarHeader() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        
        val imgPerfil: ImageView = headerView.findViewById(R.id.imgPerfilNav)
        val txtNombre: TextView = headerView.findViewById(R.id.txtNombreNav)
        val txtCorreo: TextView = headerView.findViewById(R.id.txtCorreoNav)

        val usuario = usuarioDAO.obtenerUsuarioPorId(idUsuarioLogueado)
        usuario?.let {
            txtNombre.text = it.nombres
            txtCorreo.text = it.correo
            if (!it.foto.isNullOrEmpty()) {
                try {
                    imgPerfil.setImageURI(Uri.parse(it.foto))
                } catch (e: Exception) {
                    imgPerfil.setImageResource(android.R.drawable.ic_menu_camera)
                }
            } else {
                imgPerfil.setImageResource(android.R.drawable.ic_menu_camera)
            }
        }

        imgPerfil.setOnClickListener {
            pickImageLauncher.launch("image/*")
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
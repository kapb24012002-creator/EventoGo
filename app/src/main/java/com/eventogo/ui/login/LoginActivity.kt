package com.eventogo.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eventogo.MainActivity
import com.eventogo.R
import com.eventogo.dao.UsuarioDAO

class LoginActivity : AppCompatActivity() {

    private lateinit var usuarioDAO: UsuarioDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usuarioDAO = UsuarioDAO(this)

        val edtCorreo: EditText = findViewById(R.id.edtLoginCorreo)
        val edtPass: EditText = findViewById(R.id.edtLoginPassword)
        val btnEntrar: Button = findViewById(R.id.btnLogin)
        val txtIrRegistro: TextView = findViewById(R.id.txtIrRegistro)

        btnEntrar.setOnClickListener {
            val correo = edtCorreo.text.toString()
            val pass = edtPass.text.toString()

            if (correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val usuario = usuarioDAO.login(correo, pass)
                if (usuario != null) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }
        }

        txtIrRegistro.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
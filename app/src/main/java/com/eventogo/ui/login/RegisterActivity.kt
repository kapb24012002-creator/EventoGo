package com.eventogo.ui.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eventogo.R
import com.eventogo.dao.UsuarioDAO
import com.eventogo.model.Usuario

class RegisterActivity : AppCompatActivity() {

    private lateinit var usuarioDAO: UsuarioDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usuarioDAO = UsuarioDAO(this)

        val edtNombres: EditText = findViewById(R.id.edtRegNombres)
        val edtCorreo: EditText = findViewById(R.id.edtRegCorreo)
        val edtTelefono: EditText = findViewById(R.id.edtRegTelefono)
        val edtPass: EditText = findViewById(R.id.edtRegPassword)
        val btnRegistrar: Button = findViewById(R.id.btnRegistrarUsuario)
        val txtVolver: TextView = findViewById(R.id.txtVolverLogin)

        btnRegistrar.setOnClickListener {
            val nombres = edtNombres.text.toString()
            val correo = edtCorreo.text.toString()
            val telefono = edtTelefono.text.toString()
            val pass = edtPass.text.toString()

            if (nombres.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor, completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                val nuevoUsuario = Usuario(
                    nombres = nombres,
                    correo = correo,
                    password = pass,
                    telefono = telefono,
                    fechaRegistro = "" // SQLite lo pone por defecto
                )

                val res = usuarioDAO.registrar(nuevoUsuario)
                if (res != -1L) {
                    Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al registrar (el correo podría ya existir)", Toast.LENGTH_SHORT).show()
                }
            }
        }

        txtVolver.setOnClickListener {
            finish()
        }
    }
}
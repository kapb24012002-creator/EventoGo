package com.eventogo.ui.clientes

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.eventogo.R
import com.eventogo.dao.ClienteDAO
import com.eventogo.model.Cliente

class RegistroClienteActivity : AppCompatActivity() {

    private lateinit var clienteDAO: ClienteDAO
    private var idCliente: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_cliente)

        clienteDAO = ClienteDAO(this)

        val edtNombre: EditText = findViewById(R.id.edtNombreCliente)
        val edtCorreo: EditText = findViewById(R.id.edtCorreoCliente)
        val edtTelefono: EditText = findViewById(R.id.edtTelefonoCliente)
        val edtDireccion: EditText = findViewById(R.id.edtDireccionCliente)
        val spnTipo: Spinner = findViewById(R.id.spnTipoCliente)
        val btnGuardar: Button = findViewById(R.id.btnGuardarCliente)
        val btnEliminar: Button = findViewById(R.id.btnEliminarCliente)

        val tipos = arrayOf("Persona Física", "Empresa")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        spnTipo.adapter = adapter

        // Check if editing
        idCliente = intent.getIntExtra("ID_CLIENTE", -1)
        if (idCliente != -1) {
            findViewById<TextView>(R.id.txtTituloRegistroCliente).text = "Editar Cliente"
            btnGuardar.text = "ACTUALIZAR"
            btnEliminar.visibility = android.view.View.VISIBLE
            // Normally we'd load data from DAO here
            edtNombre.setText(intent.getStringExtra("NOMBRE"))
            edtCorreo.setText(intent.getStringExtra("CORREO"))
            edtTelefono.setText(intent.getStringExtra("TELEFONO"))
            edtDireccion.setText(intent.getStringExtra("DIRECCION"))
            val tipoIdx = tipos.indexOf(intent.getStringExtra("TIPO"))
            if (tipoIdx != -1) spnTipo.setSelection(tipoIdx)
        }

        btnEliminar.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar Cliente")
                .setMessage("¿Estás seguro de que deseas eliminar este cliente?")
                .setPositiveButton("Sí") { _, _ ->
                    if (clienteDAO.eliminar(idCliente) > 0) {
                        Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        btnGuardar.setOnClickListener {
            val nombre = edtNombre.text.toString()
            val correo = edtCorreo.text.toString()
            val telefono = edtTelefono.text.toString()
            val direccion = edtDireccion.text.toString()
            val tipo = spnTipo.selectedItem.toString()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Nombre es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cliente = Cliente(
                idCliente = if (idCliente != -1) idCliente else 0,
                idUsuario = 1, // Default for now
                nombre = nombre,
                correo = correo,
                telefono = telefono,
                direccion = direccion,
                tipoCliente = tipo
            )

            val res = if (idCliente == -1) {
                clienteDAO.insertar(cliente).toInt()
            } else {
                clienteDAO.actualizar(cliente)
            }

            if (res != -1) {
                Toast.makeText(this, "Operación exitosa", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error en la operación", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
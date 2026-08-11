package com.eventogo.ui.clientes

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.eventogo.R
import com.eventogo.dao.ProveedorDAO
import com.eventogo.model.Proveedor

class RegistroProveedorActivity : AppCompatActivity() {

    private lateinit var proveedorDAO: ProveedorDAO
    private var idProveedor: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_proveedor)

        proveedorDAO = ProveedorDAO(this)

        val edtNombre: EditText = findViewById(R.id.edtNombreProveedor)
        val edtCorreo: EditText = findViewById(R.id.edtCorreoProveedor)
        val edtTelefono: EditText = findViewById(R.id.edtTelefonoProveedor)
        val edtDireccion: EditText = findViewById(R.id.edtDireccionProveedor)
        val edtServicio: EditText = findViewById(R.id.edtServicioProveedor)
        val btnGuardar: Button = findViewById(R.id.btnGuardarProveedor)
        val btnEliminar: Button = findViewById(R.id.btnEliminarProveedor)

        // Check if editing
        idProveedor = intent.getIntExtra("ID_PROVEEDOR", -1)
        if (idProveedor != -1) {
            findViewById<TextView>(R.id.txtTituloRegistroProveedor).text = "Editar Proveedor"
            btnGuardar.text = "ACTUALIZAR"
            btnEliminar.visibility = android.view.View.VISIBLE
            edtNombre.setText(intent.getStringExtra("NOMBRE"))
            edtCorreo.setText(intent.getStringExtra("CORREO"))
            edtTelefono.setText(intent.getStringExtra("TELEFONO"))
            edtDireccion.setText(intent.getStringExtra("DIRECCION"))
            edtServicio.setText(intent.getStringExtra("SERVICIO"))
        }

        btnEliminar.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar Proveedor")
                .setMessage("¿Estás seguro de que deseas eliminar este proveedor?")
                .setPositiveButton("Sí") { _, _ ->
                    if (proveedorDAO.eliminar(idProveedor) > 0) {
                        Toast.makeText(this, "Proveedor eliminado", Toast.LENGTH_SHORT).show()
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
            val servicio = edtServicio.text.toString()

            if (nombre.isEmpty() || servicio.isEmpty()) {
                Toast.makeText(this, "Nombre y Servicio son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val proveedor = Proveedor(
                idProveedor = if (idProveedor != -1) idProveedor else 0,
                idUsuario = 1, // Default for now
                nombre = nombre,
                correo = correo,
                telefono = telefono,
                direccion = direccion,
                servicio = servicio
            )

            val res = if (idProveedor == -1) {
                proveedorDAO.insertar(proveedor).toInt()
            } else {
                proveedorDAO.actualizar(proveedor)
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
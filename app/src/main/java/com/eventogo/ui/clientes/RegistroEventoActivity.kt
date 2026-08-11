package com.eventogo.ui.clientes

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.eventogo.R
import com.eventogo.dao.ClienteDAO
import com.eventogo.dao.EventoDAO
import com.eventogo.model.Cliente
import com.eventogo.model.Evento
import java.util.*

class RegistroEventoActivity : AppCompatActivity() {

    private lateinit var eventoDAO: EventoDAO
    private lateinit var clienteDAO: ClienteDAO
    private var idEvento: Int = -1
    private var listaClientes = mutableListOf<Cliente>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_evento)

        eventoDAO = EventoDAO(this)
        clienteDAO = ClienteDAO(this)

        val edtTitulo: EditText = findViewById(R.id.edtTituloEvento)
        val spnCliente: Spinner = findViewById(R.id.spnCliente)
        val edtFecha: EditText = findViewById(R.id.edtFechaEvento)
        val edtHora: EditText = findViewById(R.id.edtHoraEvento)
        val edtLugar: EditText = findViewById(R.id.edtLugarEvento)
        val edtCategoria: EditText = findViewById(R.id.edtCategoriaEvento)
        val edtDesc: EditText = findViewById(R.id.edtDescripcionEvento)
        val btnGuardar: Button = findViewById(R.id.btnGuardarEvento)
        val btnEliminar: Button = findViewById(R.id.btnEliminarEvento)

        // Load clients into spinner
        listaClientes = clienteDAO.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaClientes.map { it.nombre })
        spnCliente.adapter = adapter

        // Date Picker
        edtFecha.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                edtFecha.setText("$year-${month + 1}-$day")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Time Picker
        edtHora.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                edtHora.setText(String.format("%02d:%02d", hour, minute))
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        // Check if editing
        idEvento = intent.getIntExtra("ID_EVENTO", -1)
        if (idEvento != -1) {
            findViewById<TextView>(R.id.txtTituloRegistroEvento).text = "Editar Evento"
            btnGuardar.text = "ACTUALIZAR"
            btnEliminar.visibility = android.view.View.VISIBLE
            edtTitulo.setText(intent.getStringExtra("TITULO"))
            edtFecha.setText(intent.getStringExtra("FECHA"))
            edtHora.setText(intent.getStringExtra("HORA"))
            edtLugar.setText(intent.getStringExtra("LUGAR"))
            edtCategoria.setText(intent.getStringExtra("CATEGORIA"))
            edtDesc.setText(intent.getStringExtra("DESCRIPCION"))
            
            val clienteId = intent.getIntExtra("ID_CLIENTE", -1)
            val clienteIdx = listaClientes.indexOfFirst { it.idCliente == clienteId }
            if (clienteIdx != -1) spnCliente.setSelection(clienteIdx)
        }

        btnEliminar.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar Evento")
                .setMessage("¿Estás seguro de que deseas eliminar este evento?")
                .setPositiveButton("Sí") { _, _ ->
                    if (eventoDAO.eliminar(idEvento) > 0) {
                        Toast.makeText(this, "Evento eliminado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        btnGuardar.setOnClickListener {
            val titulo = edtTitulo.text.toString()
            val fecha = edtFecha.text.toString()
            val hora = edtHora.text.toString()
            val lugar = edtLugar.text.toString()
            val cat = edtCategoria.text.toString()
            val desc = edtDesc.text.toString()
            
            if (titulo.isEmpty() || listaClientes.isEmpty()) {
                Toast.makeText(this, "Título y Cliente son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val clienteSeleccionado = listaClientes[spnCliente.selectedItemPosition]

            val evento = Evento(
                idEvento = if (idEvento != -1) idEvento else 0,
                idUsuario = 1,
                idCliente = clienteSeleccionado.idCliente,
                titulo = titulo,
                descripcion = desc,
                fecha = fecha,
                hora = hora,
                lugar = lugar,
                categoria = cat,
                estado = intent.getStringExtra("ESTADO") ?: "Pendiente"
            )

            val res = if (idEvento == -1) {
                eventoDAO.insertar(evento).toInt()
            } else {
                eventoDAO.actualizar(evento)
            }

            if (res != -1) {
                Toast.makeText(this, "Éxito", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
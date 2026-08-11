package com.eventogo.ui.clientes

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.eventogo.R
import com.eventogo.dao.ClienteDAO
import com.eventogo.dao.PagoDAO
import com.eventogo.model.Cliente
import com.eventogo.model.Pago
import java.util.*

class RegistroPagoActivity : AppCompatActivity() {

    private lateinit var pagoDAO: PagoDAO
    private lateinit var clienteDAO: ClienteDAO
    private var idPago: Int = -1
    private var listaClientes = mutableListOf<Cliente>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_pago)

        pagoDAO = PagoDAO(this)
        clienteDAO = ClienteDAO(this)

        val spnCliente: Spinner = findViewById(R.id.spnClientePago)
        val edtMonto: EditText = findViewById(R.id.edtMontoPago)
        val edtFecha: EditText = findViewById(R.id.edtFechaPago)
        val edtMetodo: EditText = findViewById(R.id.edtMetodoPago)
        val spnEstado: Spinner = findViewById(R.id.spnEstadoPago)
        val btnGuardar: Button = findViewById(R.id.btnGuardarPago)
        val btnEliminar: Button = findViewById(R.id.btnEliminarPago)

        // Load clients
        listaClientes = clienteDAO.obtenerTodos()
        val adapterCli = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaClientes.map { it.nombre })
        spnCliente.adapter = adapterCli

        // States
        val estados = arrayOf("Pendiente", "Completado", "Cancelado")
        val adapterEst = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)
        spnEstado.adapter = adapterEst

        // Date Picker
        edtFecha.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                edtFecha.setText("$year-${month + 1}-$day")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Check if editing
        idPago = intent.getIntExtra("ID_PAGO", -1)
        if (idPago != -1) {
            findViewById<TextView>(R.id.txtTituloRegistroPago).text = "Editar Pago"
            btnGuardar.text = "ACTUALIZAR"
            btnEliminar.visibility = android.view.View.VISIBLE
            edtMonto.setText(intent.getDoubleExtra("MONTO", 0.0).toString())
            edtFecha.setText(intent.getStringExtra("FECHA"))
            edtMetodo.setText(intent.getStringExtra("METODO"))
            
            val clienteId = intent.getIntExtra("ID_CLIENTE", -1)
            val cliIdx = listaClientes.indexOfFirst { it.idCliente == clienteId }
            if (cliIdx != -1) spnCliente.setSelection(cliIdx)
            
            val estIdx = estados.indexOf(intent.getStringExtra("ESTADO"))
            if (estIdx != -1) spnEstado.setSelection(estIdx)
        }

        btnEliminar.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar Pago")
                .setMessage("¿Estás seguro de que deseas eliminar este registro de pago?")
                .setPositiveButton("Sí") { _, _ ->
                    if (pagoDAO.eliminar(idPago) > 0) {
                        Toast.makeText(this, "Pago eliminado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        btnGuardar.setOnClickListener {
            val montoStr = edtMonto.text.toString()
            val fecha = edtFecha.text.toString()
            val metodo = edtMetodo.text.toString()
            val estado = spnEstado.selectedItem.toString()
            
            if (montoStr.isEmpty() || listaClientes.isEmpty()) {
                Toast.makeText(this, "Monto y Cliente son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val clienteSel = listaClientes[spnCliente.selectedItemPosition]

            val pago = Pago(
                idPago = if (idPago != -1) idPago else 0,
                idCliente = clienteSel.idCliente,
                monto = montoStr.toDouble(),
                fechaPago = fecha,
                metodoPago = metodo,
                estadoPago = estado
            )

            val res = if (idPago == -1) {
                pagoDAO.insertar(pago).toInt()
            } else {
                pagoDAO.actualizar(pago)
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
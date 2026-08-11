package com.eventogo.ui.clientes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.eventogo.R
import com.eventogo.dao.ClienteDAO
import com.eventogo.dao.PagoDAO
import com.eventogo.model.Pago

class PagoAdapter(private var pagos: MutableList<Pago>) : 
    RecyclerView.Adapter<PagoAdapter.PagoViewHolder>() {

    class PagoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtCliente: TextView = view.findViewById(R.id.txtClientePago)
        val txtMonto: TextView = view.findViewById(R.id.txtMontoPago)
        val txtFecha: TextView = view.findViewById(R.id.txtFechaPago)
        val txtEstado: TextView = view.findViewById(R.id.txtEstadoPagoTag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pago, parent, false)
        return PagoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagoViewHolder, position: Int) {
        val pago = pagos[position]
        
        val clienteDAO = ClienteDAO(holder.itemView.context)
        val listaClientes = clienteDAO.obtenerTodos()
        val cliente = listaClientes.find { it.idCliente == pago.idCliente }
        
        holder.txtCliente.text = cliente?.nombre ?: "Cliente Desconocido"
        holder.txtMonto.text = "$ ${String.format("%.2f", pago.monto)}"
        holder.txtFecha.text = pago.fechaPago
        holder.txtEstado.text = "Estado: ${pago.estadoPago}"
        
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, RegistroPagoActivity::class.java)
            intent.putExtra("ID_PAGO", pago.idPago)
            intent.putExtra("ID_CLIENTE", pago.idCliente)
            intent.putExtra("MONTO", pago.monto)
            intent.putExtra("FECHA", pago.fechaPago)
            intent.putExtra("METODO", pago.metodoPago)
            intent.putExtra("ESTADO", pago.estadoPago)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Pago")
                .setMessage("¿Estás seguro de que deseas eliminar este registro de pago?")
                .setPositiveButton("Sí") { _, _ ->
                    val dao = PagoDAO(holder.itemView.context)
                    if (dao.eliminar(pago.idPago) > 0) {
                        pagos.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, pagos.size)
                        Toast.makeText(holder.itemView.context, "Pago eliminado", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
    }

    override fun getItemCount() = pagos.size
}
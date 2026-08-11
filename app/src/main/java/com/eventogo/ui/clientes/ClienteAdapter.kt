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
import com.eventogo.model.Cliente

class ClienteAdapter(private var clientes: MutableList<Cliente>) : 
    RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombreCliente)
        val txtCorreo: TextView = view.findViewById(R.id.txtCorreoCliente)
        val txtTipo: TextView = view.findViewById(R.id.txtTipoClienteTag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.txtNombre.text = cliente.nombre
        holder.txtCorreo.text = cliente.correo
        holder.txtTipo.text = cliente.tipoCliente
        
        if (cliente.tipoCliente.contains("Empresa", ignoreCase = true)) {
            holder.txtTipo.setBackgroundResource(R.drawable.bg_tag_empresa)
        } else {
            holder.txtTipo.setBackgroundResource(R.drawable.bg_tag_persona)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, RegistroClienteActivity::class.java)
            intent.putExtra("ID_CLIENTE", cliente.idCliente)
            intent.putExtra("NOMBRE", cliente.nombre)
            intent.putExtra("CORREO", cliente.correo)
            intent.putExtra("TELEFONO", cliente.telefono)
            intent.putExtra("DIRECCION", cliente.direccion)
            intent.putExtra("TIPO", cliente.tipoCliente)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Cliente")
                .setMessage("¿Estás seguro de que deseas eliminar a ${cliente.nombre}?")
                .setPositiveButton("Sí") { _, _ ->
                    val dao = ClienteDAO(holder.itemView.context)
                    if (dao.eliminar(cliente.idCliente) > 0) {
                        clientes.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, clientes.size)
                        Toast.makeText(holder.itemView.context, "Cliente eliminado", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
    }

    override fun getItemCount() = clientes.size
}
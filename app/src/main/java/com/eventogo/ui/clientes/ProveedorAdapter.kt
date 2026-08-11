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
import com.eventogo.dao.ProveedorDAO
import com.eventogo.model.Proveedor

class ProveedorAdapter(private var proveedores: MutableList<Proveedor>) : 
    RecyclerView.Adapter<ProveedorAdapter.ProveedorViewHolder>() {

    class ProveedorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombreProveedor)
        val txtTelefono: TextView = view.findViewById(R.id.txtTelefonoProveedor)
        val txtServicio: TextView = view.findViewById(R.id.txtServicioTag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProveedorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_proveedor, parent, false)
        return ProveedorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProveedorViewHolder, position: Int) {
        val proveedor = proveedores[position]
        holder.txtNombre.text = proveedor.nombre
        holder.txtTelefono.text = proveedor.telefono
        holder.txtServicio.text = "Servicio: ${proveedor.servicio}"
        
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, RegistroProveedorActivity::class.java)
            intent.putExtra("ID_PROVEEDOR", proveedor.idProveedor)
            intent.putExtra("NOMBRE", proveedor.nombre)
            intent.putExtra("CORREO", proveedor.correo)
            intent.putExtra("TELEFONO", proveedor.telefono)
            intent.putExtra("DIRECCION", proveedor.direccion)
            intent.putExtra("SERVICIO", proveedor.servicio)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Proveedor")
                .setMessage("¿Estás seguro de que deseas eliminar a ${proveedor.nombre}?")
                .setPositiveButton("Sí") { _, _ ->
                    val dao = ProveedorDAO(holder.itemView.context)
                    if (dao.eliminar(proveedor.idProveedor) > 0) {
                        proveedores.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, proveedores.size)
                        Toast.makeText(holder.itemView.context, "Proveedor eliminado", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
    }

    override fun getItemCount() = proveedores.size
}
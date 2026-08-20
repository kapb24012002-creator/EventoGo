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
import com.eventogo.dao.EventoDAO
import com.eventogo.model.Evento

class EventoAdapter(private var eventos: MutableList<Evento>) : 
    RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    class EventoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTitulo: TextView = view.findViewById(R.id.txtTituloEvento)
        val txtFecha: TextView = view.findViewById(R.id.txtFechaEvento)
        val txtHora: TextView = view.findViewById(R.id.txtHoraEvento)
        val txtEstado: TextView = view.findViewById(R.id.txtEstadoEventoTag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventos[position]
        holder.txtTitulo.text = evento.titulo
        holder.txtFecha.text = evento.fecha
        holder.txtHora.text = evento.hora
        holder.txtEstado.text = "Estado: ${evento.estado}"

        // Estilo dinámico según el estado
        when (evento.estado) {
            "Atrasado" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_tag_danger)
                holder.txtEstado.setTextColor(androidx.core.content.ContextCompat.getColor(holder.itemView.context, R.color.danger_red))
            }
            "Finalizado" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_tag_success)
                holder.txtEstado.setTextColor(androidx.core.content.ContextCompat.getColor(holder.itemView.context, R.color.success_text))
            }
            "En Progreso" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_tag_servicio)
                holder.txtEstado.setTextColor(androidx.core.content.ContextCompat.getColor(holder.itemView.context, R.color.primary_magenta))
            }
            else -> { // Pendiente
                holder.txtEstado.setBackgroundResource(R.drawable.bg_tag_servicio)
                holder.txtEstado.setTextColor(androidx.core.content.ContextCompat.getColor(holder.itemView.context, R.color.gray_text))
            }
        }
        
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, RegistroEventoActivity::class.java)
            intent.putExtra("ID_EVENTO", evento.idEvento)
            intent.putExtra("TITULO", evento.titulo)
            intent.putExtra("ID_CLIENTE", evento.idCliente)
            intent.putExtra("FECHA", evento.fecha)
            intent.putExtra("HORA", evento.hora)
            intent.putExtra("LUGAR", evento.lugar)
            intent.putExtra("DESCRIPCION", evento.descripcion)
            intent.putExtra("CATEGORIA", evento.categoria)
            intent.putExtra("ESTADO", evento.estado)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Evento")
                .setMessage("¿Estás seguro de que deseas eliminar el evento ${evento.titulo}?")
                .setPositiveButton("Sí") { _, _ ->
                    val dao = EventoDAO(holder.itemView.context)
                    if (dao.eliminar(evento.idEvento) > 0) {
                        eventos.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, eventos.size)
                        Toast.makeText(holder.itemView.context, "Evento eliminado", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
    }

    override fun getItemCount() = eventos.size
}
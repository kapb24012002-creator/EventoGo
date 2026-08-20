package com.eventogo.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eventogo.R
import com.eventogo.dao.EventoDAO
import com.eventogo.dao.SummaryDAO
import com.eventogo.model.Evento
import com.eventogo.ui.clientes.EventoAdapter
import com.eventogo.ui.clientes.RegistroEventoActivity
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var summaryDAO: SummaryDAO
    private lateinit var eventoDAO: EventoDAO
    private lateinit var rvEvents: RecyclerView
    private lateinit var txtSelectedDate: TextView
    private var allEvents = mutableListOf<Evento>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        
        summaryDAO = SummaryDAO(requireContext())
        eventoDAO = EventoDAO(requireContext())
        
        val txtCountEventos: TextView = root.findViewById(R.id.txtCountEventos)
        val txtCountClientes: TextView = root.findViewById(R.id.txtCountClientes)
        val txtCountProveedores: TextView = root.findViewById(R.id.txtCountProveedores)
        val btnCrear: Button = root.findViewById(R.id.btnCrearNuevoEvento)
        val calendar: CalendarView = root.findViewById(R.id.calendarViewDashboard)
        txtSelectedDate = root.findViewById(R.id.txtSelectedDate)
        rvEvents = root.findViewById(R.id.rvEventsByDate)
        
        rvEvents.layoutManager = LinearLayoutManager(context)

        btnCrear.setOnClickListener {
            val intent = Intent(requireContext(), RegistroEventoActivity::class.java)
            startActivity(intent)
        }
        
        // Initial data load
        refreshDashboardData(txtCountEventos, txtCountClientes, txtCountProveedores)

        // Calendar listener
        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            filterEventsByDate(selectedDate)
        }

        // Default: filter by today
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        filterEventsByDate(today)

        requestNotificationPermission()
        
        return root
    }

    private fun requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            if (androidx.core.content.ContextCompat.checkSelfPermission(requireContext(), permission) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permission), 101)
            }
        }
    }

    private fun refreshDashboardData(tE: TextView, tC: TextView, tP: TextView) {
        val counts = summaryDAO.getCounts()
        tE.text = (counts["eventos"] ?: 0).toString()
        tC.text = (counts["clientes"] ?: 0).toString()
        tP.text = (counts["proveedores"] ?: 0).toString()
        
        allEvents = eventoDAO.obtenerTodos()
    }

    private fun filterEventsByDate(date: String) {
        val filteredList = allEvents.filter { it.fecha == date }.toMutableList()
        if (filteredList.isEmpty()) {
            txtSelectedDate.text = "Sin eventos para el: $date"
            txtSelectedDate.setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.silver_text))
        } else {
            txtSelectedDate.text = "Eventos para el: $date (${filteredList.size})"
            txtSelectedDate.setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.primary_magenta))
        }
        rvEvents.adapter = EventoAdapter(filteredList)
    }

    override fun onResume() {
        super.onResume()
        // Refresh everything when coming back
        val counts = summaryDAO.getCounts()
        view?.let { root ->
            root.findViewById<TextView>(R.id.txtCountEventos).text = (counts["eventos"] ?: 0).toString()
            root.findViewById<TextView>(R.id.txtCountClientes).text = (counts["clientes"] ?: 0).toString()
            root.findViewById<TextView>(R.id.txtCountProveedores).text = (counts["proveedores"] ?: 0).toString()
            
            allEvents = eventoDAO.obtenerTodos()
            
            // Get currently selected calendar date or just today
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            filterEventsByDate(today)
        }
    }
}
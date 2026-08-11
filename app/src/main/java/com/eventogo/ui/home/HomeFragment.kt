package com.eventogo.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.eventogo.R
import com.eventogo.dao.SummaryDAO
import com.eventogo.ui.clientes.RegistroEventoActivity

class HomeFragment : Fragment() {

    private lateinit var summaryDAO: SummaryDAO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        
        summaryDAO = SummaryDAO(requireContext())
        
        val txtCountEventos: TextView = root.findViewById(R.id.txtCountEventos)
        val txtCountClientes: TextView = root.findViewById(R.id.txtCountClientes)
        val txtCountProveedores: TextView = root.findViewById(R.id.txtCountProveedores)
        val btnCrear: Button = root.findViewById(R.id.btnCrearNuevoEvento)

        btnCrear.setOnClickListener {
            val intent = Intent(requireContext(), RegistroEventoActivity::class.java)
            startActivity(intent)
        }
        
        val counts = summaryDAO.getCounts()
        
        txtCountEventos.text = (counts["eventos"] ?: 0).toString()
        txtCountClientes.text = (counts["clientes"] ?: 0).toString()
        txtCountProveedores.text = (counts["proveedores"] ?: 0).toString()
        
        return root
    }

    override fun onResume() {
        super.onResume()
        // Refresh counts when returning to home
        summaryDAO = SummaryDAO(requireContext())
        val counts = summaryDAO.getCounts()
        view?.findViewById<TextView>(R.id.txtCountEventos)?.text = (counts["eventos"] ?: 0).toString()
        view?.findViewById<TextView>(R.id.txtCountClientes)?.text = (counts["clientes"] ?: 0).toString()
        view?.findViewById<TextView>(R.id.txtCountProveedores)?.text = (counts["proveedores"] ?: 0).toString()
    }
}
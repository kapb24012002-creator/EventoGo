package com.eventogo.ui.clientes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.eventogo.R
import com.eventogo.dao.EventoDAO

class EventosFragment : Fragment() {

    private lateinit var eventoDAO: EventoDAO
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_eventos, container, false)
        
        eventoDAO = EventoDAO(requireContext())
        recyclerView = root.findViewById(R.id.rvEventos)
        recyclerView.layoutManager = LinearLayoutManager(context)
        
        val fab: FloatingActionButton = root.findViewById(R.id.fabAddEvento)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), RegistroEventoActivity::class.java)
            startActivity(intent)
        }

        cargarEventos()
        
        return root
    }

    override fun onResume() {
        super.onResume()
        cargarEventos()
    }

    private fun cargarEventos() {
        val lista = eventoDAO.obtenerTodos()
        recyclerView.adapter = EventoAdapter(lista)
    }
}
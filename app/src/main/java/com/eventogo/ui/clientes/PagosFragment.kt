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
import com.eventogo.dao.PagoDAO

class PagosFragment : Fragment() {

    private lateinit var pagoDAO: PagoDAO
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_pagos, container, false)
        
        pagoDAO = PagoDAO(requireContext())
        recyclerView = root.findViewById(R.id.rvPagos)
        recyclerView.layoutManager = LinearLayoutManager(context)
        
        val fab: FloatingActionButton = root.findViewById(R.id.fabAddPago)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), RegistroPagoActivity::class.java)
            startActivity(intent)
        }

        cargarPagos()
        
        return root
    }

    override fun onResume() {
        super.onResume()
        cargarPagos()
    }

    private fun cargarPagos() {
        val lista = pagoDAO.obtenerTodos()
        recyclerView.adapter = PagoAdapter(lista)
    }
}
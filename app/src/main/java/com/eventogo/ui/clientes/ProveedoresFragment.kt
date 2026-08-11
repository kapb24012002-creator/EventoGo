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
import com.eventogo.dao.ProveedorDAO

class ProveedoresFragment : Fragment() {

    private lateinit var proveedorDAO: ProveedorDAO
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_proveedores, container, false)
        
        proveedorDAO = ProveedorDAO(requireContext())
        recyclerView = root.findViewById(R.id.rvProveedores)
        recyclerView.layoutManager = LinearLayoutManager(context)
        
        val fab: FloatingActionButton = root.findViewById(R.id.fabAddProveedor)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), RegistroProveedorActivity::class.java)
            startActivity(intent)
        }

        cargarProveedores()
        
        return root
    }

    override fun onResume() {
        super.onResume()
        cargarProveedores()
    }

    private fun cargarProveedores() {
        val lista = proveedorDAO.obtenerTodos()
        recyclerView.adapter = ProveedorAdapter(lista)
    }
}
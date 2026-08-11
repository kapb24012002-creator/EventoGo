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
import com.eventogo.dao.ClienteDAO

class ClientesFragment : Fragment() {

    private lateinit var clienteDAO: ClienteDAO
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_clientes, container, false)
        
        clienteDAO = ClienteDAO(requireContext())
        recyclerView = root.findViewById(R.id.rvClientes)
        recyclerView.layoutManager = LinearLayoutManager(context)
        
        val fab: FloatingActionButton = root.findViewById(R.id.fabAddCliente)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), RegistroClienteActivity::class.java)
            startActivity(intent)
        }

        cargarClientes()
        
        return root
    }

    override fun onResume() {
        super.onResume()
        cargarClientes()
    }

    private fun cargarClientes() {
        val lista = clienteDAO.obtenerTodos()
        recyclerView.adapter = ClienteAdapter(lista)
    }
}
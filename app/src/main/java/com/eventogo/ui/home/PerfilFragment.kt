package com.eventogo.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.eventogo.MainActivity
import com.eventogo.R
import com.eventogo.dao.UsuarioDAO

class PerfilFragment : Fragment() {

    private lateinit var usuarioDAO: UsuarioDAO
    private var idUsuarioLogueado: Int = -1
    private lateinit var imgPerfil: ImageView

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            requireContext().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val path = it.toString()
            usuarioDAO.actualizarFoto(idUsuarioLogueado, path)
            
            // Actualizar la foto en esta pantalla
            imgPerfil.setImageURI(it)
            
            // Sincronizar con el menú lateral de MainActivity
            (activity as? MainActivity)?.actualizarHeader()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_perfil, container, false)

        val prefs = requireContext().getSharedPreferences("sesion_usuario", android.content.Context.MODE_PRIVATE)
        idUsuarioLogueado = prefs.getInt("id_usuario", -1)
        usuarioDAO = UsuarioDAO(requireContext())

        imgPerfil = root.findViewById(R.id.imgPerfilDetalle)
        val txtNombre: TextView = root.findViewById(R.id.txtNombrePerfil)
        val txtCorreo: TextView = root.findViewById(R.id.txtCorreoPerfil)
        val txtTelefono: TextView = root.findViewById(R.id.txtTelefonoPerfil)
        val txtFecha: TextView = root.findViewById(R.id.txtFechaRegistroPerfil)
        val btnCambiar: Button = root.findViewById(R.id.btnCambiarFotoPerfil)

        cargarDatos()

        btnCambiar.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        return root
    }

    private fun cargarDatos() {
        val usuario = usuarioDAO.obtenerUsuarioPorId(idUsuarioLogueado)
        usuario?.let {
            view?.findViewById<TextView>(R.id.txtNombrePerfil)?.text = it.nombres
            view?.findViewById<TextView>(R.id.txtCorreoPerfil)?.text = it.correo
            view?.findViewById<TextView>(R.id.txtTelefonoPerfil)?.text = if (it.telefono.isNotEmpty()) it.telefono else "No registrado"
            view?.findViewById<TextView>(R.id.txtFechaRegistroPerfil)?.text = it.fechaRegistro
            
            if (!it.foto.isNullOrEmpty()) {
                try {
                    imgPerfil.setImageURI(Uri.parse(it.foto))
                } catch (e: Exception) {
                    imgPerfil.setImageResource(android.R.drawable.ic_menu_camera)
                }
            }
        }
    }
}
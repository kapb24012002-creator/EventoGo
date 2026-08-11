package com.eventogo.model

data class Proveedor(
    var idProveedor: Int = 0,
    var idUsuario: Int,
    var nombre: String,
    var correo: String,
    var telefono: String,
    var direccion: String,
    var servicio: String
)
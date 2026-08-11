package com.eventogo.model

data class Cliente(
    var idCliente: Int = 0,
    var idUsuario: Int,
    var nombre: String,
    var correo: String,
    var telefono: String,
    var direccion: String,
    var tipoCliente: String
)
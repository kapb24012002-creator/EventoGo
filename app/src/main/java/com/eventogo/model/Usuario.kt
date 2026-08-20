package com.eventogo.model

data class Usuario(
    var idUsuario: Int = 0,
    var nombres: String,
    var correo: String,
    var password: String,
    var telefono: String,
    var fechaRegistro: String,
    var foto: String? = null
)
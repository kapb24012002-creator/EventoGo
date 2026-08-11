package com.eventogo.model

data class Evento(
    var idEvento: Int = 0,
    var idUsuario: Int,
    var idCliente: Int,
    var titulo: String,
    var descripcion: String,
    var fecha: String,
    var hora: String,
    var lugar: String,
    var categoria: String,
    var estado: String
)
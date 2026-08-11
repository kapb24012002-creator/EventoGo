package com.eventogo.model

data class Pago(
    var idPago: Int = 0,
    var idCliente: Int,
    var monto: Double,
    var fechaPago: String,
    var metodoPago: String,
    var estadoPago: String
)
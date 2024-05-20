package com.example.mjosevl_20240518


data class TicketAereo(
    val numeroBoleto: String,
    val pasajero: String,
    val vuelo: Vuelo,
    val fechaCompra: String
)

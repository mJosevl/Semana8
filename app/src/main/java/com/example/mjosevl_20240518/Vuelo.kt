package com.example.mjosevl_20240518
import java.io.Serializable

data class Vuelo(
    val id: Int,
    val destino: String,
    val precio: Double,
    val horaSalida: String,
    val horaLlegada: String
) : Serializable
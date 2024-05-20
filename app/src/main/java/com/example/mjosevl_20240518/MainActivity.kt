package com.example.mjosevl_20240518

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "promo_notifications"
    private val vuelos = listOf(
        Vuelo(1, "Santiago", 50000.0, "10:00", "12:00"),
        Vuelo(2, "Valparaíso", 40000.0, "14:00", "15:00"),
        Vuelo(3, "Concepción", 45000.0, "16:00", "18:00")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        // Manejo del intent para mostrar la información del vuelo
        val vuelo = intent.getSerializableExtra("vuelo") as? Vuelo
        vuelo?.let {
            updateVueloInfo(it)
        }

        // Iniciar el temporizador para enviar notificaciones cada 120 segundos
        fixedRateTimer("timer", false, 0L, 120000) {
            runOnUiThread {
                sendPromotionNotification()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showPromotion(view: View) {
        val vuelo = vuelos[Random.nextInt(vuelos.size)]
        updateVueloInfo(vuelo)
    }

    private fun sendPromotionNotification() {
        val vuelo = vuelos[Random.nextInt(vuelos.size)]
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("vuelo", vuelo)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_)
            .setContentTitle("Nueva Promoción: ${vuelo.destino}!")
            .setContentText("Vuelo a ${vuelo.destino} por solo \$${vuelo.precio}. Salida: ${vuelo.horaSalida}.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    private fun updateVueloInfo(vuelo: Vuelo) {
        findViewById<TextView>(R.id.vueloInfo).text = "Destino: ${vuelo.destino}\nPrecio: \$${vuelo.precio}\nSalida: ${vuelo.horaSalida}\nLlegada: ${vuelo.horaLlegada}"
    }
}

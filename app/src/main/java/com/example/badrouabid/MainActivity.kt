package com.example.badrouabid

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var numeroAleatorio = 0
    private var intentos = 0
    private var rangoMin = 1
    private var rangoMax = 100
    private val ranking = ArrayList<Pair<String, Int>>()

    private var etNumero: EditText? = null
    private var btnIntentar: Button? = null
    private var tvIntentos: TextView? = null
    private var tvMensaje: TextView? = null
    private var tvHistorial: TextView? = null
    private var scrollViewHistorial: ScrollView? = null
    private var btnRanking: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializar widgets
        etNumero = findViewById(R.id.etNumero)
        btnIntentar = findViewById(R.id.btnIntentar)
        btnRanking = findViewById(R.id.btnRanking)
        tvIntentos = findViewById(R.id.tvIntentos)
        tvMensaje = findViewById(R.id.textView)
        tvHistorial = findViewById(R.id.tvHistorial)
        scrollViewHistorial = findViewById(R.id.scrollViewHistorial)

        generarNumeroAleatorio()

        // Botón para abrir Hall of Fame
        btnRanking?.setOnClickListener {
            // Anyadimos un ranking para poder probar en caso de querer comprobar que se añaden cosas al ranking
            if (ranking.isEmpty()) {
                ranking.add("Alice" to 3)
                ranking.add("Bob" to 5)
                ranking.add("Carlos" to 2)
            }
            // ordenamos siempre por cantidad de intentos antes de transformarlo en string
            ranking.sortBy { it.second }

            // Convertimos Pair<String, Int> a ArrayList<String>
            val rankingStrings = ArrayList<String>()
            ranking.forEach { (nombre, intentos) ->
                val safeNombre = nombre.ifEmpty { "Anonimo" }
                val safeIntentos = intentos.takeIf { it >= 0 } ?: 0
                rankingStrings.add("$safeNombre:$safeIntentos")
            }

            val intent = Intent(this, HallOfFameActivity::class.java)
            intent.putStringArrayListExtra("ranking", rankingStrings)
            startActivity(intent)
        }

        // Boton de intentar adivinar
        btnIntentar?.setOnClickListener {
            val numero = etNumero?.text.toString()
            if (numero.isEmpty()) {
                Toast.makeText(this, "Introduce un número", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // como introducimos un string, lo convertimos en un int para poder hacer comparacion
            val numeroIntento = numero.toInt()
            intentos++
            tvIntentos?.text = "Intentos: $intentos"

            when {
                numeroIntento < numeroAleatorio -> {
                    if (rangoMin <= numeroIntento) {
                        rangoMin = numeroIntento + 1
                        tvMensaje?.text = "El número está entre $rangoMin y $rangoMax"
                    }
                }
                numeroIntento > numeroAleatorio -> {
                    if (rangoMax >= numeroIntento) {
                        rangoMax = numeroIntento - 1
                        tvMensaje?.text = "El número está entre $rangoMin y $rangoMax"
                    }
                }
                else -> {
                    // Ganaste
                    val inputNombre = EditText(this)
                    inputNombre.hint = "Tu nombre"

                    AlertDialog.Builder(this).setTitle("¡Felicidades!")
                        .setMessage("Has adivinado el número en $intentos intentos.\nIntroduce tu nombre:")
                        .setView(inputNombre)
                        .setPositiveButton("Aceptar") { dialog, _ ->
                            val nombreJugador = inputNombre.text.toString().ifEmpty { "Anonimo" }
                            ranking.add(nombreJugador to intentos)

                            generarNumeroAleatorio()
                            rangoMin = 1
                            rangoMax = 100
                            tvMensaje?.text = "Adivina el número entre $rangoMin y $rangoMax"
                            tvIntentos?.text = "Intentos: 0"
                            etNumero?.text?.clear()
                            etNumero?.requestFocus()
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }

            // Añadir al historial
            tvHistorial?.append("Intento $intentos: $numeroIntento\n")
            scrollViewHistorial?.post {
                scrollViewHistorial?.fullScroll(ScrollView.FOCUS_DOWN)
            }

            etNumero?.text?.clear()
            etNumero?.requestFocus()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun generarNumeroAleatorio() {
        numeroAleatorio = (1..100).random()
        intentos = 0
    }
}

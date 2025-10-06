package com.example.badrouabid

import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HallOfFameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hall_of_fame)

        // Ajuste de margenes por barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tableLayout = findViewById<TableLayout>(R.id.tableRanking)
        val rankingStrings = intent.getStringArrayListExtra("ranking") ?: ArrayList()

        // Llenar tabla con ranking
        rankingStrings.forEachIndexed { index, s ->
            val parts = s.split(":")
            val nombre = parts[0]
            val intentos = parts.getOrElse(1) { "0" }

            val row = TableRow(this)
            val tvIndex = TextView(this).apply { text = "${index + 1}" }
            val tvNombre = TextView(this).apply { text = nombre }
            val tvIntentos = TextView(this).apply { text = intentos }

            row.addView(tvIndex)
            row.addView(tvNombre)
            row.addView(tvIntentos)
            tableLayout.addView(row)
        }

        // Botón para volver a MainActivity
        val btnVolver = findViewById<Button>(R.id.btnJuego)
        btnVolver.setOnClickListener {
            finish()
        }
    }
}

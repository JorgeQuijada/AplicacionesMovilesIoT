package com.example.controlaccesoiot

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        val etPin = findViewById<EditText>(R.id.etPin)
        val btnVerificar = findViewById<Button>(R.id.btnVerificarPin)
        val btnVolver = findViewById<Button>(R.id.btnVolverPanel)

        btnVerificar.setOnClickListener {
            val pin = etPin.text.toString()
            if (pin == "1234") { // PIN de tu Arduino Maestro
                Toast.makeText(this, "PIN Correcto. Acceso Concedido.", Toast.LENGTH_SHORT).show()
                finish() // Vuelve al panel
            } else {
                Toast.makeText(this, "PIN Incorrecto", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}
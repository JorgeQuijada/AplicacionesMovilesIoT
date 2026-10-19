package com.example.controlaccesoiot

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val usuario = intent.getStringExtra("usuario") ?: "Desconocido"
        val tvUsuarioPerfil = findViewById<TextView>(R.id.tvUsuarioPerfil)
        val etCambioPass = findViewById<EditText>(R.id.etCambioPass)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCambios)
        val btnVolver = findViewById<Button>(R.id.btnVolverPanel)

        tvUsuarioPerfil.text = "Usuario: $usuario"

        btnGuardar.setOnClickListener {
            val nuevaPass = etCambioPass.text.toString()
            if (nuevaPass.length < 4) {
                Toast.makeText(this, "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("UsuariosDB", Context.MODE_PRIVATE)
            prefs.edit().putString("user_$usuario", hashPassword(nuevaPass)).apply()

            Toast.makeText(this, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show()
            finish() // Volvemos al panel
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
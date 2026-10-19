package com.example.controlaccesoiot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val usuariosPrefs = getSharedPreferences("UsuariosDB", Context.MODE_PRIVATE)
        if (!usuariosPrefs.contains("user_admin")) {
            usuariosPrefs.edit().putString("user_admin", hashPassword("1234")).apply()
        }

        val prefs = getSharedPreferences("SesionApp", Context.MODE_PRIVATE)
        val usuarioGuardado = prefs.getString("usuarioLogueado", null)

        if (usuarioGuardado != null) {
            mostrarPanelControl(usuarioGuardado)
        } else {
            mostrarLogin()
        }
    }

    private fun mostrarLogin() {
        setContentView(R.layout.activity_login)

        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val btnIrARegistro = findViewById<Button>(R.id.btnIrARegistro)
        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnIngresar.setOnClickListener {
            val user = etUsuario.text.toString().trim()
            val pass = etPassword.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passHasheada = hashPassword(pass)
            val prefs = getSharedPreferences("UsuariosDB", Context.MODE_PRIVATE)
            val passGuardada = prefs.getString("user_$user", null)

            if (passGuardada != null && passGuardada == passHasheada) {
                val sesionPrefs = getSharedPreferences("SesionApp", Context.MODE_PRIVATE)
                sesionPrefs.edit().putString("usuarioLogueado", user).apply()
                mostrarPanelControl(user)
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        btnIrARegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun mostrarPanelControl(usuario: String) {
        setContentView(R.layout.activity_main)

        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val tvEstado = findViewById<TextView>(R.id.tvEstado)
        val tvIconoCandado = findViewById<TextView>(R.id.tvIconoCandado)
        val tvLogs = findViewById<TextView>(R.id.tvLogs)
        val btnAbrir = findViewById<Button>(R.id.btnAbrir)
        val btnCerrar = findViewById<Button>(R.id.btnCerrar)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnPerfil = findViewById<Button>(R.id.btnPerfil)
        val btnIrAPin = findViewById<Button>(R.id.btnIrAPin)

        tvBienvenida.text = "Hola, $usuario"
        agregarLog(tvLogs, "Sesión iniciada.")

        btnIrAPin.setOnClickListener {
            startActivity(Intent(this, PinActivity::class.java))
        }

        btnAbrir.setOnClickListener {
            lifecycleScope.launch {
                tvEstado.text = "ABRIENDO..."
                agregarLog(tvLogs, "Enviando comando OPEN...")
                delay(1000)

                tvEstado.text = "ABIERTO"
                tvEstado.setTextColor(android.graphics.Color.parseColor("#10B981"))
                tvIconoCandado.text = "🔓"
                tvIconoCandado.setBackgroundResource(R.drawable.bg_circle_green)

                agregarLog(tvLogs, "✅ Puerta abierta.")
            }
        }

        btnCerrar.setOnClickListener {
            lifecycleScope.launch {
                tvEstado.text = "CERRANDO..."
                agregarLog(tvLogs, "Enviando comando CLOSE...")
                delay(1000)

                tvEstado.text = "CERRADO"
                tvEstado.setTextColor(android.graphics.Color.parseColor("#EF4444"))
                tvIconoCandado.text = "🔒"
                tvIconoCandado.setBackgroundResource(R.drawable.bg_circle_red)

                agregarLog(tvLogs, "✅ Puerta cerrada.")
            }
        }

        btnPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val prefs = getSharedPreferences("SesionApp", Context.MODE_PRIVATE)
            prefs.edit().remove("usuarioLogueado").apply() // Limpiamos la sesión
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            mostrarLogin() // Volvemos a la pantalla de login
        }
    }

    private fun agregarLog(tvLogs: TextView, mensaje: String) {
        val horaActual = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        tvLogs.append("\n[$horaActual] > $mensaje")
        val scrollView = tvLogs.parent as android.widget.ScrollView
        scrollView.post { scrollView.fullScroll(android.view.View.FOCUS_DOWN) }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
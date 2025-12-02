package com.sportydev.carnerolearnrenewed

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast // Importante para feedback visual
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. INICIALIZAR TTS MANAGER (Una sola línea)
        TtsManager.initialize(this)

        setupBottomNavigation()
        setupWordOfTheDay() // Aquí usamos el TTS
        setupHeaderButtons()
    }

    // Lógica para la Palabra del Día
    private fun setupWordOfTheDay() {
        val btnAudio = findViewById<ImageButton>(R.id.btnWodListen)
        val tvWord = findViewById<TextView>(R.id.tvWodEnglish)

        btnAudio.setOnClickListener {
            val wordToSpeak = tvWord.text.toString()

            // 2. USAR EL TTS MANAGER (Súper simple)
            TtsManager.speak(wordToSpeak)

            // Feedback visual opcional
            Toast.makeText(this, "🔊 $wordToSpeak", Toast.LENGTH_SHORT).show()
        }
    }

    // Configuración del botón de Settings en el Header
    private fun setupHeaderButtons() {
        findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))        }

        // Configuración del botón grande "Next Lesson"
        findViewById<FloatingActionButton>(R.id.btnStartLesson).setOnClickListener {
            startActivity(Intent(this, StudyBookActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        // ... (Tu código de navegación igual que antes) ...
        findViewById<LinearLayout>(R.id.nav_home).setOnClickListener { }

        findViewById<LinearLayout>(R.id.nav_study).setOnClickListener {
            startActivity(Intent(this, StudyBookActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.nav_vocabulary).setOnClickListener {
            startActivity(Intent(this, VocabularyActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.nav_listening).setOnClickListener {
            startActivity(Intent(this, ListeningActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.nav_reading).setOnClickListener {
            startActivity(Intent(this, ReadingActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }

    // 3. LIMPIEZA DE RECURSOS
    override fun onDestroy() {
        // Solo si quieres matar el TTS al salir de la app por completo.
        // Si quieres que siga disponible en otras pantallas, no llames a shutdown() aquí,
        // sino en la Application class o deja que Android lo maneje.
        TtsManager.stop()
        super.onDestroy()
    }
}
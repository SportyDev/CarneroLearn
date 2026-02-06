package com.sportydev.carnerolearnrenewed

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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

        TtsManager.initialize(this)

        setupBottomNavigation()
        setupWordOfTheDay()
        setupHeaderButtons()
    }

    // Metodo para configurar la palabra del dia
    private fun setupWordOfTheDay() {
        val btnAudio = findViewById<ImageButton>(R.id.btnWodListen)
        val tvWord = findViewById<TextView>(R.id.tvWodEnglish)

        btnAudio.setOnClickListener {
            val wordToSpeak = tvWord.text.toString()

            TtsManager.speak(wordToSpeak)

            Toast.makeText(this, " $wordToSpeak", Toast.LENGTH_SHORT).show()
        }
    }

    // Metodo para los botones del header, el boton de settings y el
    private fun setupHeaderButtons() {
        findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))        }

        // Configuracion del boton grande "Next Lesson"
        findViewById<FloatingActionButton>(R.id.btnStartLesson).setOnClickListener {
            startActivity(Intent(this, StudyBookActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
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

    // LIMPIEZA DE RECURSOS
    override fun onDestroy() {

        TtsManager.stop()
        super.onDestroy()
    }
}
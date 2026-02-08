package com.sportydev.carnerolearnrenewed.ui.reading

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity
import com.sportydev.carnerolearnrenewed.ui.quiz.QuizActivity
import com.sportydev.carnerolearnrenewed.utils.TtsManager

class ReadingDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading_detail)

        // 1. Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }

        // 2. Datos
        val title = intent.getStringExtra("STORY_TITLE") ?: "The Lost Key"
        val content = intent.getStringExtra("STORY_CONTENT") ?: "Content..."
        val level = intent.getStringExtra("STORY_LEVEL") ?: "Beginner"

        // 3. Views
        findViewById<TextView>(R.id.tvStoryTitle).text = title
        findViewById<TextView>(R.id.tvStoryContent).text = content
        findViewById<Chip>(R.id.chipLevel).text = level

        // 4. Lógica Botón LISTEN (El de arriba)
        val fabListen = findViewById<FloatingActionButton>(R.id.fabListenStory)

        // Inicializar TTS
        TtsManager.initialize(this)

        fabListen.setOnClickListener {
            // Feedback visual
            Toast.makeText(this, "Reading aloud...", Toast.LENGTH_SHORT).show()
            TtsManager.speak(content)
        }

        val fabQuiz = findViewById<ExtendedFloatingActionButton>(R.id.btnStartQuiz)

        fabQuiz.setOnClickListener {
            val currentStoryTitle = intent.getStringExtra("STORY_TITLE") ?: "The Lost Key"

            val intent = Intent(this, QuizActivity::class.java)

            // Usamos "QUIZ_TOPIC" porque es lo que QuizActivity espera recibir
            intent.putExtra("QUIZ_TOPIC", currentStoryTitle)

            startActivity(intent)

            // Ejemplo futuro:
            // val intent = Intent(this, QuizActivity::class.java)
            // intent.putExtra("QUIZ_ID", "quiz_lost_key")
            // startActivity(intent)
        }
    }

    override fun onDestroy() {
        TtsManager.stop()
        super.onDestroy()
    }
}
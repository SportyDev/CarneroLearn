package com.sportydev.carnerolearnrenewed.ui.reading

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.local.AdminBd
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity
import com.sportydev.carnerolearnrenewed.ui.quiz.QuizActivity
import com.sportydev.carnerolearnrenewed.utils.TtsManager

class ReadingDetailActivity : BaseActivity() {

    private lateinit var adminBd: AdminBd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reading_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_reading_detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adminBd = AdminBd(this)
        TtsManager.initialize(this)

        val storyId = intent.getIntExtra("EXTRA_STORY_ID", -1)
        if (storyId != -1) {
            loadStory(storyId)
        }

        // 1. Declarar las variables
        val fabTakeQuiz =
            findViewById<com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton>(
                R.id.fabTakeQuiz
            )
        val adminBd = AdminBd(this)

        // Asumiendo que recibes el ID y Título del cuento desde el Intent anterior:
        val storyTitle = intent.getStringExtra("STORY_TITLE") ?: "Reading"

        // 2. Revisar si hay preguntas en la base de datos para este cuento
        if (storyId != -1) {
            val questionCount = adminBd.getQuestionCountForContext("READING", storyId)

            if (questionCount > 0) {
                // Si hay preguntas, mostramos el botón
                fabTakeQuiz.visibility = View.VISIBLE

                // 3. Configurar el clic para abrir el Quiz
                fabTakeQuiz.setOnClickListener {
                    val intent = Intent(this, QuizActivity::class.java)
                    intent.putExtra(
                        "QUIZ_TOPIC",
                        storyTitle
                    ) // El título del cuento aparecerá en el badge del quiz
                    intent.putExtra("EXTRA_CONTEXT_TYPE", "READING")
                    intent.putExtra("EXTRA_CONTEXT_ID", storyId)
                    startActivity(intent)
                }
            }
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun loadStory(storyId: Int) {
        val story = adminBd.getReadingResourceById(storyId) ?: return

        val tvTitle = findViewById<TextView>(R.id.tvStoryTitle)
        val tvContent = findViewById<TextView>(R.id.tvStoryContent)
        val tvDurationDetail = findViewById<TextView>(R.id.tvStoryDurationDetail)
        val chipLevel = findViewById<Chip>(R.id.chipLevel)
        val fabListen = findViewById<FloatingActionButton>(R.id.fabListenStory)

        tvTitle.text = story.title
        tvContent.text = story.contentText
        tvDurationDetail.text = "${story.durationRead ?: 0} min read"
        chipLevel.text = story.level

        // Aplicar color al chip según el nivel
        val levelColor = when (story.level?.lowercase()) {
            "beginner" -> "#4CAF50"
            "intermediate" -> "#FF9800"
            "advanced" -> "#F44336"
            else -> "#607D8B"
        }
        chipLevel.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(levelColor))

        fabListen.setOnClickListener {
            story.contentText?.let { text ->
                TtsManager.speak(text)
            }
        }
    }

    override fun onDestroy() {
        TtsManager.stop()
        super.onDestroy()
    }
}
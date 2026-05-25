package com.sportydev.carnerolearnrenewed.ui.grammar

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.local.AdminBd
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity
import com.sportydev.carnerolearnrenewed.ui.quiz.QuizActivity

class TopicDetail : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_topic_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Inicializar Base de Datos
        val adminBd = AdminBd(this)

        // 2. Obtener referencias
        val headerBg = findViewById<View>(R.id.headerBackground)
        val ivWatermark = findViewById<ImageView>(R.id.ivWatermark)
        val tvTitle = findViewById<TextView>(R.id.tvTopicTitle)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val fabPractice = findViewById<ExtendedFloatingActionButton>(R.id.btnStartQuiz)

        // Vistas de contenido
        val tvStructure = findViewById<TextView>(R.id.tvGrammarStructure)
        val tvContent = findViewById<TextView>(R.id.tvDetailContent)
        val tvEx1 = findViewById<TextView>(R.id.tvExample1)
        val tvEx2 = findViewById<TextView>(R.id.tvExample2)
        val tvEx3 = findViewById<TextView>(R.id.tvExample3)
        val tvMistake = findViewById<TextView>(R.id.tvCommonMistake)

        // Etiquetas para colorear (Labels)
        val lblStructure = findViewById<TextView>(R.id.lblStructure)
        val lblExplanation = findViewById<TextView>(R.id.lblExplanation)
        val lblExamples = findViewById<TextView>(R.id.lblExamples)

        // 3. Obtener datos del Intent
        // IMPORTANTE: Necesitamos recibir el ID del tema desde la pantalla anterior
        val topicId = intent.getIntExtra("EXTRA_ID", -1)
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "Grammar Topic"
        val structure = intent.getStringExtra("EXTRA_STRUCTURE")
        val content = intent.getStringExtra("EXTRA_CONTENT")
        val ex1 = intent.getStringExtra("EXTRA_EXAMPLE_1")
        val ex2 = intent.getStringExtra("EXTRA_EXAMPLE_2")
        val ex3 = intent.getStringExtra("EXTRA_EXAMPLE_3")
        val mistake = intent.getStringExtra("EXTRA_MISTAKE")

        val colorHex = intent.getStringExtra("EXTRA_COLOR") ?: "#5C6BC0"
        val iconRes = intent.getIntExtra("EXTRA_ICON", R.drawable.ic_time)

// Pon esto temporalmente para depurar:
//        android.widget.Toast.makeText(
//            this,
//            "ID recibido: $topicId, Preguntas: ${
//                adminBd.getQuestionCountForContext(
//                    "Grammar",
//                    topicId
//                )
//            }",
//            android.widget.Toast.LENGTH_LONG
//        ).show()

        // 4. Aplicar datos a las vistas
        tvTitle.text = title
        tvStructure.text = structure
        tvContent.text = content
        tvMistake.text = mistake

        // Ejemplos dinámicos
        if (!ex1.isNullOrEmpty()) {
            tvEx1.text = ex1; tvEx1.visibility = View.VISIBLE
        } else {
            tvEx1.visibility = View.GONE
        }
        if (!ex2.isNullOrEmpty()) {
            tvEx2.text = ex2; tvEx2.visibility = View.VISIBLE
        } else {
            tvEx2.visibility = View.GONE
        }
        if (!ex3.isNullOrEmpty()) {
            tvEx3.text = ex3; tvEx3.visibility = View.VISIBLE
        } else {
            tvEx3.visibility = View.GONE
        }

        // 5. Aplicar Estilos Dinámicos (Color e Icono)
        try {
            val color = Color.parseColor(colorHex)
            headerBg.backgroundTintList = ColorStateList.valueOf(color)
            ivWatermark.setImageResource(iconRes)
            lblStructure.setTextColor(color)
            lblExplanation.setTextColor(color)
            lblExamples.setTextColor(color)

            // Que el botón combine con el color del tema
            fabPractice.backgroundTintList = ColorStateList.valueOf(color)
        } catch (e: Exception) {
            e.printStackTrace()
        }

// --- LÓGICA DEL QUIZ DINÁMICO (VERSIÓN FINAL) ---
        if (topicId != -1) {
            // Ya no importa si le pasas "GRAMMAR", "Grammar" o "grammar"
            val questionCount = adminBd.getQuestionCountForContext("GRAMMAR", topicId)

            if (questionCount > 0) {
                // Mostrar el botón
                fabPractice.visibility = View.VISIBLE
                fabPractice.alpha = 1f // Nos aseguramos de que no sea transparente

                fabPractice.setOnClickListener {
                    val quizIntent = Intent(this, QuizActivity::class.java)
                    quizIntent.putExtra("QUIZ_TOPIC", title)
                    quizIntent.putExtra("EXTRA_CONTEXT_TYPE", "GRAMMAR")
                    quizIntent.putExtra("EXTRA_CONTEXT_ID", topicId)
                    startActivity(quizIntent)
                }
            } else {
                fabPractice.visibility = View.GONE
            }
        } else {
            fabPractice.visibility = View.GONE
        }


        // Botón atrás
        btnBack.setOnClickListener { finish() }
    }
}
package com.sportydev.carnerolearnrenewed.ui.vocabulary

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.local.AdminBd
import com.sportydev.carnerolearnrenewed.ui.adapters.WordAdapter
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity
import com.sportydev.carnerolearnrenewed.ui.quiz.QuizActivity
import com.sportydev.carnerolearnrenewed.utils.TtsManager

class VocabularyDetailActivity : BaseActivity() {

    private lateinit var adminBd: AdminBd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vocabulary_detail)

        // Inicializa BD y TTS
        adminBd = AdminBd(this)
        TtsManager.initialize(this)

        // 1. Recibe datos del intent (IMPORTANTE: Asegúrate de enviar el EXTRA_CATEGORY_ID desde tu adaptador)
        val categoryId = intent.getIntExtra("EXTRA_CATEGORY_ID", -1)
        val categoryName = intent.getStringExtra("EXTRA_CATEGORY_NAME") ?: "Vocabulary"
        val colorHex = intent.getStringExtra("EXTRA_COLOR_HEX") ?: "#4A90E2"

        // 2. Configurar Header
        setupHeader(categoryName, colorHex)

        // 3. Cargar palabras de la Base de Datos real
        val wordsList = adminBd.getWordsByCategoryName(categoryName)

        // 4. Configurar RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvWords)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = WordAdapter(wordsList, colorHex)

        // 5. Configurar el FAB del Quiz Dinámico
        val fabPractice = findViewById<ExtendedFloatingActionButton>(R.id.btnStartQuiz)

        try {
            // Pintamos el botón del mismo color de la categoría
            fabPractice.backgroundTintList = ColorStateList.valueOf(Color.parseColor(colorHex))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Si tenemos un ID válido y la categoría tiene palabras, mostramos el botón
        if (categoryId != -1 && wordsList.isNotEmpty()) {
            fabPractice.visibility = View.VISIBLE
            fabPractice.alpha = 1f

            fabPractice.setOnClickListener {
                val quizIntent = Intent(this, QuizActivity::class.java)
                quizIntent.putExtra("QUIZ_TOPIC", categoryName)
                quizIntent.putExtra("EXTRA_CONTEXT_TYPE", "Vocabulary") // Llama a la fábrica mágica
                quizIntent.putExtra("EXTRA_CONTEXT_ID", categoryId)
                startActivity(quizIntent)
            }
        } else {
            fabPractice.visibility = View.GONE
        }

        // Botón de retroceso
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }
    }

    private fun setupHeader(title: String, color: String) {
        val headerBg = findViewById<View>(R.id.headerBackground)
        val tvTitle = findViewById<TextView>(R.id.tvCategoryTitle)
        val ivWatermark = findViewById<ImageView>(R.id.ivWatermark)

        tvTitle.text = title

        try {
            val parsedColor = Color.parseColor(color)
            headerBg.backgroundTintList = ColorStateList.valueOf(parsedColor)

            when (title) {
                "Airport" -> ivWatermark.setImageResource(R.drawable.ic_airport)
                "Hotel" -> ivWatermark.setImageResource(R.drawable.ic_hotel)
                "Restaurant" -> ivWatermark.setImageResource(R.drawable.ic_restaurant)
                "Technology" -> ivWatermark.setImageResource(R.drawable.ic_technology)
                "Sports" -> ivWatermark.setImageResource(R.drawable.ic_sports)
                "Business" -> ivWatermark.setImageResource(R.drawable.ic_business)
                "Health" -> ivWatermark.setImageResource(R.drawable.ic_health)
                "Education" -> ivWatermark.setImageResource(R.drawable.ic_education)
                // Si no coincide, dejará el icono por defecto de tu XML
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        TtsManager.stop()
        adminBd.close()
        super.onDestroy()
    }
}
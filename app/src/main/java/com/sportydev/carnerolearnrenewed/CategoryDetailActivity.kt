package com.sportydev.carnerolearnrenewed

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
import com.google.android.material.progressindicator.LinearProgressIndicator

class CategoryDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_detail)

        // 0. Inicializar TTS (Para que el adaptador pueda usarlo)
        TtsManager.initialize(this)

        // 1. Recibir datos del Intent
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Vocabulary"
        val colorHex = intent.getStringExtra("CATEGORY_COLOR") ?: "#4A90E2"

        // 2. Configurar UI
        setupHeader(categoryName, colorHex)

        // 3. Obtener datos y configurar RecyclerView
        val wordsList = getMockWords(categoryName)
        val recyclerView = findViewById<RecyclerView>(R.id.rvWords)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Pasamos la lista al adaptador. El adaptador usará TtsManager internamente.
        recyclerView.adapter = WordAdapter(wordsList)

        // 4. Configurar Botón Atrás
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun setupHeader(title: String, color: String) {
        val headerBg = findViewById<View>(R.id.headerBackground)
        val tvTitle = findViewById<TextView>(R.id.tvCategoryTitle)
        val progressBar = findViewById<LinearProgressIndicator>(R.id.progressBar)
        val ivWatermark = findViewById<ImageView>(R.id.ivWatermark)
        val fabPractice = findViewById<ExtendedFloatingActionButton>(R.id.btnStartQuiz)

        tvTitle.text = title

        try {
            val parsedColor = Color.parseColor(color)

            // Aplicar colores
            headerBg.backgroundTintList = ColorStateList.valueOf(parsedColor)
            fabPractice.backgroundTintList = ColorStateList.valueOf(parsedColor)
            progressBar.setIndicatorColor(parsedColor)

            // Cambiar icono según categoría
            when (title) {
                "Airport" -> ivWatermark.setImageResource(R.drawable.ic_airport)
                "Hotel" -> ivWatermark.setImageResource(R.drawable.ic_hotel)
                "Restaurant" -> ivWatermark.setImageResource(R.drawable.ic_restaurant)
                "Technology" -> ivWatermark.setImageResource(R.drawable.ic_technology)
                "Sports" -> ivWatermark.setImageResource(R.drawable.ic_sports)
                "Business" -> ivWatermark.setImageResource(R.drawable.ic_business)
                "Health" -> ivWatermark.setImageResource(R.drawable.ic_health)
                "Education" -> ivWatermark.setImageResource(R.drawable.ic_education)
                // Agrega defaults si faltan
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // --- LÓGICA DEL BOTÓN START QUIZ ---
        fabPractice.setOnClickListener {
            // Enviamos el nombre de la categoría (ej: "Airport") al Quiz
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("QUIZ_TOPIC", title)
            startActivity(intent)
        }
    }

    // BASE DE DATOS TEMPORAL
    private fun getMockWords(category: String): List<Word> {
        val list = mutableListOf<Word>()
        when (category) {
            "Airport" -> {
                list.add(
                    Word(
                        "Boarding Pass",
                        "Pase de abordar",
                        "/ˈbɔːrdɪŋ pæs/",
                        "noun",
                        "A pass for boarding an aircraft.",
                        "Please show your boarding pass."
                    )
                )
                list.add(
                    Word(
                        "Luggage",
                        "Equipaje",
                        "/ˈlʌɡɪdʒ/",
                        "noun",
                        "Suitcases or bags for travelling.",
                        "Check your luggage here."
                    )
                )
                list.add(
                    Word(
                        "Gate",
                        "Puerta",
                        "/ɡeɪt/",
                        "noun",
                        "Exit to the aircraft.",
                        "Go to Gate 4."
                    )
                )
            }

            "Hotel" -> {
                list.add(
                    Word(
                        "Check-in",
                        "Registro",
                        "/ˈtʃek ɪn/",
                        "verb",
                        "Arrive and register.",
                        "Check-in is at 3 PM."
                    )
                )
                list.add(
                    Word(
                        "Reservation",
                        "Reservación",
                        "/ˌrezərˈveɪʃn/",
                        "noun",
                        "Booking a room.",
                        "I have a reservation."
                    )
                )
            }

            "Technology" -> {
                list.add(
                    Word(
                        "Software",
                        "Software",
                        "/ˈsɒftweə/",
                        "noun",
                        "Programs for a computer.",
                        "Update your software."
                    )
                )
                list.add(
                    Word(
                        "Database",
                        "Base de datos",
                        "/ˈdeɪtəbeɪs/",
                        "noun",
                        "Structured data storage.",
                        "The database is online."
                    )
                )
            }
            // ... Agrega más categorías si quieres ...
            else -> {
                list.add(
                    Word(
                        "Example",
                        "Ejemplo",
                        "/ɪɡˈzæmpl/",
                        "noun",
                        "A representative item.",
                        "This is an example."
                    )
                )
            }
        }
        return list
    }

    override fun onDestroy() {
        // Opcional: Detener TTS si sales de la categoría
        TtsManager.stop()
        super.onDestroy()
    }
}
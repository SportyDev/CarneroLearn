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
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.sportydev.carnerolearnrenewed.ui.quiz.QuizActivity
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.local.AdminBd
import com.sportydev.carnerolearnrenewed.data.model.Word
import com.sportydev.carnerolearnrenewed.ui.adapters.WordAdapter
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity
import com.sportydev.carnerolearnrenewed.utils.TtsManager

class CategoryDetailActivity : BaseActivity() {

    private lateinit var adminBd: AdminBd // Instancia de la base de datos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_detail)

        // Inicializa BD y TTS
        adminBd = AdminBd(this)
        TtsManager.initialize(this)

        // Recibe datos del intent
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Vocabulary"
        val colorHex = intent.getStringExtra("CATEGORY_COLOR") ?: "#4A90E2"

        setupHeader(categoryName, colorHex)

        // 1. INTENTAR CARGAR DESDE LA BASE DE DATOS
        var wordsList = adminBd.getWordsByCategoryName(categoryName)

        // 2. SI LA BD ESTÁ VACÍA, USAR DATOS DE PRUEBA (Para que no veas la pantalla blanca)
        if (wordsList.isEmpty()) {
            // Toast.makeText(this, "Cargando datos de prueba (BD vacía)", Toast.LENGTH_SHORT).show()
            wordsList = getMockWordsUpdated(categoryName)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rvWords)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = WordAdapter(wordsList)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }
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
            headerBg.backgroundTintList = ColorStateList.valueOf(parsedColor)
            fabPractice.backgroundTintList = ColorStateList.valueOf(parsedColor)
            progressBar.setIndicatorColor(parsedColor)

            when (title) {
                "Airport" -> ivWatermark.setImageResource(R.drawable.ic_airport)
                "Hotel" -> ivWatermark.setImageResource(R.drawable.ic_hotel)
                "Restaurant" -> ivWatermark.setImageResource(R.drawable.ic_restaurant)
                "Technology" -> ivWatermark.setImageResource(R.drawable.ic_technology)
                "Sports" -> ivWatermark.setImageResource(R.drawable.ic_sports)
                "Business" -> ivWatermark.setImageResource(R.drawable.ic_business)
                "Health" -> ivWatermark.setImageResource(R.drawable.ic_health)
                "Education" -> ivWatermark.setImageResource(R.drawable.ic_education)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        fabPractice.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("QUIZ_TOPIC", title)
            startActivity(intent)
        }
    }

    // HE CORREGIDO ESTA FUNCIÓN PARA QUE COINCIDA CON TU NUEVO MODELO 'WORD'
    // Ponemos id = 0 y categoryId = 0 porque son datos falsos
    private fun getMockWordsUpdated(category: String): List<Word> {
        val list = mutableListOf<Word>()
        when (category) {
            "Airport" -> {
                list.add(
                    Word(
                        0,
                        0,
                        "Boarding Pass",
                        "Pase de abordar",
                        "/ˈbɔːrdɪŋ pæs/",
                        "noun",
                        "Please show your boarding pass."
                    )
                )
                list.add(
                    Word(
                        0,
                        0,
                        "Luggage",
                        "Equipaje",
                        "/ˈlʌɡɪdʒ/",
                        "noun",
                        "Check your luggage here."
                    )
                )
                list.add(Word(0, 0, "Gate", "Puerta", "/ɡeɪt/", "noun", "Go to Gate 4."))
            }
            "Hotel" -> {
                list.add(
                    Word(
                        0,
                        0,
                        "Check-in",
                        "Registro",
                        "/ˈtʃek ɪn/",
                        "verb",
                        "Check-in is at 3 PM."
                    )
                )
                list.add(
                    Word(
                        0,
                        0,
                        "Reservation",
                        "Reservación",
                        "/ˌrezərˈveɪʃn/",
                        "noun",
                        "I have a reservation."
                    )
                )
            }
            "Technology" -> {
                list.add(
                    Word(
                        0,
                        0,
                        "Software",
                        "Software",
                        "/ˈsɒftweə/",
                        "noun",
                        "Update your software."
                    )
                )
                list.add(
                    Word(
                        0,
                        0,
                        "Database",
                        "Base de datos",
                        "/ˈdeɪtəbeɪs/",
                        "noun",
                        "The database is online."
                    )
                )
            }
            else -> {
                list.add(
                    Word(
                        0,
                        0,
                        "Example",
                        "Ejemplo",
                        "/ɪɡˈzæmpl/",
                        "noun",
                        "This is an example."
                    )
                )
            }
        }
        return list
    }

    override fun onDestroy() {
        TtsManager.stop()
        adminBd.close() // Cerramos la conexión a BD
        super.onDestroy()
    }
}
package com.sportydev.carnerolearnrenewed.ui.grammar

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
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity

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

        // 1. Obtener referencias
        val headerBg = findViewById<View>(R.id.headerBackground)
        val ivWatermark = findViewById<ImageView>(R.id.ivWatermark)
        val tvTitle = findViewById<TextView>(R.id.tvTopicTitle)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

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

        // 2. Obtener datos del Intent
        val title = intent.getStringExtra("EXTRA_TITLE")
        val structure = intent.getStringExtra("EXTRA_STRUCTURE")
        val content = intent.getStringExtra("EXTRA_CONTENT")
        val ex1 = intent.getStringExtra("EXTRA_EXAMPLE_1")
        val ex2 = intent.getStringExtra("EXTRA_EXAMPLE_2")
        val ex3 = intent.getStringExtra("EXTRA_EXAMPLE_3")
        val mistake = intent.getStringExtra("EXTRA_MISTAKE")

        val colorHex = intent.getStringExtra("EXTRA_COLOR") ?: "#5C6BC0"
        val iconRes = intent.getIntExtra("EXTRA_ICON", R.drawable.ic_time)

        // 3. Aplicar datos
        tvTitle.text = title
        tvStructure.text = structure
        tvContent.text = content
        tvEx1.text = ex1
        tvEx2.text = ex2
        tvEx3.text = ex3
        tvMistake.text = mistake

        // 4. Aplicar Estilos Dinamicos (Color y Icono)
        try {
            val color = Color.parseColor(colorHex)

            // Fondo Curvo Header
            headerBg.backgroundTintList = ColorStateList.valueOf(color)

            // Icono Marca de Agua
            ivWatermark.setImageResource(iconRes)

            // Colorear los títulos de las secciones para que combinen
            lblStructure.setTextColor(color)
            lblExplanation.setTextColor(color)
            lblExamples.setTextColor(color)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        // boton atras
        btnBack.setOnClickListener { finish() }
    }
}
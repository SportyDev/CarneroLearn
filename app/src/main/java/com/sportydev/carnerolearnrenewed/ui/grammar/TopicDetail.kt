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
import com.sportydev.carnerolearnrenewed.data.local.AdminBd
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity

class TopicDetail : BaseActivity() {

    private lateinit var adminBd: AdminBd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_topic_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adminBd = AdminBd(this)

        // Obtener ID
        val topicId = intent.getIntExtra("TOPIC_ID", -1)

        if (topicId == -1) {
            finish()
            return
        }

        // Buscar en la bse de datos
        val topic = adminBd.getAllGrammarTopics().find { it.id == topicId }

        if (topic == null) {
            finish()
            return
        }

        // Referencias
        val headerBg = findViewById<View>(R.id.headerBackground)
        val ivWatermark = findViewById<ImageView>(R.id.ivWatermark)
        val tvTitle = findViewById<TextView>(R.id.tvTopicTitle)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        val tvStructure = findViewById<TextView>(R.id.tvGrammarStructure)
        val tvContent = findViewById<TextView>(R.id.tvDetailContent)
        val tvEx1 = findViewById<TextView>(R.id.tvExample1)
        val tvEx2 = findViewById<TextView>(R.id.tvExample2)
        val tvEx3 = findViewById<TextView>(R.id.tvExample3)
        val tvMistake = findViewById<TextView>(R.id.tvCommonMistake)

        val lblStructure = findViewById<TextView>(R.id.lblStructure)
        val lblExplanation = findViewById<TextView>(R.id.lblExplanation)
        val lblExamples = findViewById<TextView>(R.id.lblExamples)

        // Aplicar datos
        tvTitle.text = topic.title
        tvStructure.text = topic.structureFormula
        tvContent.text = topic.explanation

        // Separar ejemplos por punto
        // Separar ejemplos por punto
        val exampleList = topic.examples?.split(".") ?: emptyList()

        tvEx1.text = exampleList.getOrNull(0)?.trim() ?: ""
        tvEx2.text = exampleList.getOrNull(1)?.trim() ?: ""
        tvEx3.text = exampleList.getOrNull(2)?.trim() ?: ""

        tvMistake.text = "" // si luego agregas columna mistake la ponemos aquí

        // Estilo dinámico fijo
        val color = Color.parseColor("#5C6BC0")

        headerBg.backgroundTintList = ColorStateList.valueOf(color)
        lblStructure.setTextColor(color)
        lblExplanation.setTextColor(color)
        lblExamples.setTextColor(color)
        ivWatermark.setImageResource(R.drawable.ic_time)

        btnBack.setOnClickListener { finish() }
    }
}
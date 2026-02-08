package com.sportydev.carnerolearnrenewed.ui.grammar

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.ui.reading.ReadingActivity
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity
import com.sportydev.carnerolearnrenewed.ui.listening.ListeningActivity
import com.sportydev.carnerolearnrenewed.ui.main.MainActivity
import com.sportydev.carnerolearnrenewed.ui.vocabulary.VocabularyActivity

class StudyBookActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_study_book)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupTopicCards()
        setupBottomNavigation()
    }

    //con esta funcion cada vez que el evento se dispara condfigura con ayuda la funcion open topic
    private fun setupTopicCards() {
        // 1. Past Continuous (Azul)
        findViewById<MaterialCardView>(R.id.cardPastContinuous).setOnClickListener {
            openTopic(
                title = "Past Continuous",
                structure = "Sujeto + Was/Were + Verbo(-ing)",
                content = "Usamos este tiempo para acciones que estaban ocurriendo en un momento específico del pasado.",
                ex1 = "I was studying when you called.",
                ex2 = "They were playing soccer at 5 PM.",
                ex3 = "Was she sleeping all day?",
                mistake = "No uses 'Was' con You, We o They. Siempre usa 'Were'.",
                colorHex = "#5C6BC0",
                iconRes = R.drawable.ic_time
            )
        }

        // 2. Future (Morado)
        findViewById<MaterialCardView>(R.id.cardFuture).setOnClickListener {
            openTopic(
                title = "Future Forms",
                structure = "Will + Verbo / Be + Going to + Verbo",
                content = "WILL: Decisiones espontáneas o predicciones.\nGOING TO: Planes ya decididos.",
                ex1 = "I think it will rain later.",
                ex2 = "Look at those clouds! It's going to rain.",
                ex3 = "I am going to visit Paris.",
                mistake = "No uses 'Will' para planes organizados. Usa 'Going to'.",
                colorHex = "#AB47BC",
                iconRes = R.drawable.ic_rocket
            )
        }

        // 3. Present Perfect (Naranja)
        findViewById<MaterialCardView>(R.id.cardPresentPerfect).setOnClickListener {
            openTopic(
                title = "Present Perfect",
                structure = "Sujeto + Have/Has + Participio Pasado",
                content = "Conecta el pasado con el presente. Experiencias de vida o acciones recientes.",
                ex1 = "I have lost my keys.",
                ex2 = "She has lived here for 10 years.",
                ex3 = "Have you ever been to Mexico?",
                mistake = "No uses tiempos específicos como 'Yesterday'.",
                colorHex = "#FFA726",
                iconRes = R.drawable.ic_checkmark
            )
        }

        // 4. Simple Past (Rojo)
        findViewById<MaterialCardView>(R.id.cardSimplePast).setOnClickListener {
            openTopic(
                title = "Simple Past",
                structure = "Verbo + ed / Irregulares",
                content = "Acciones terminadas en un tiempo específico del pasado.",
                ex1 = "I visited my grandma yesterday.",
                ex2 = "She went to the cinema.",
                ex3 = "Did you buy the car?",
                mistake = "En preguntas (Did), el verbo va en forma base: 'Did you go'.",
                colorHex = "#EF5350",
                iconRes = R.drawable.ic_history
            )
        }

        // 5. Conditionals (Verde Azulado)
        findViewById<MaterialCardView>(R.id.cardConditionals).setOnClickListener {
            openTopic(
                title = "Conditionals",
                structure = "If + Condición, Resultado",
                content = "Zero: Hechos reales.\nFirst: Futuro probable.\nSecond: Hipotético.",
                ex1 = "If you heat ice, it melts.",
                ex2 = "If it rains, I will stay home.",
                ex3 = "If I were rich, I would travel.",
                mistake = "En el 2do condicional, usa 'If I were', no 'If I was'.",
                colorHex = "#26A69A",
                iconRes = R.drawable.ic_conditional
            )
        }

        // 6. Passive Voice (Gris Azulado)
        findViewById<MaterialCardView>(R.id.cardPassiveVoice).setOnClickListener {
            openTopic(
                title = "Passive Voice",
                structure = "Objeto + To Be + Participio (+ by Sujeto)",
                content = "Enfocamos la atención en la acción o el objeto, no en quién la realiza.",
                ex1 = "The book was written by J.K. Rowling.",
                ex2 = "English is spoken worldwide.",
                ex3 = "My car has been stolen.",
                mistake = "Cuidado con los tiempos del verbo To Be.",
                colorHex = "#78909C",
                iconRes = R.drawable.ic_arrows_swap
            )
        }

        // 7. Reported Speech (Naranja Oscuro)
        findViewById<MaterialCardView>(R.id.cardReportedSpeech).setOnClickListener {
            openTopic(
                title = "Reported Speech",
                structure = "Sujeto + Said + (That) + Verbo (Backshift)",
                content = "Para contar lo que alguien más dijo. El tiempo verbal suele ir al pasado.",
                ex1 = "She said that she was tired.",
                ex2 = "He told me he had bought a car.",
                ex3 = "They asked where I lived.",
                mistake = "Cambia 'Tomorrow' por 'The next day'.",
                colorHex = "#FF7043",
                iconRes = R.drawable.ic_quote
            )
        }

        // 8. Modal Verbs (Morado Oscuro)
        findViewById<MaterialCardView>(R.id.cardModalVerbs).setOnClickListener {
            openTopic(
                title = "Modal Verbs",
                structure = "Sujeto + Modal + Verbo Base",
                content = "MUST: Obligación.\nSHOULD: Consejo.\nHAVE TO: Reglas externas.",
                ex1 = "You must wear a seatbelt.",
                ex2 = "You should visit the dentist.",
                ex3 = "Students have to wear uniforms.",
                mistake = "Nunca uses 'to' después de un modal (excepto have to).",
                colorHex = "#5E35B1",
                iconRes = R.drawable.ic_book
            )
        }
    }


    private fun openTopic(
        title: String,
        structure: String,
        content: String,
        ex1: String,
        ex2: String,
        ex3: String,
        mistake: String,
        colorHex: String,
        iconRes: Int
    ) {
        val intent = Intent(this, TopicDetail::class.java)
        intent.putExtra("EXTRA_TITLE", title)
        intent.putExtra("EXTRA_STRUCTURE", structure)
        intent.putExtra("EXTRA_CONTENT", content)
        intent.putExtra("EXTRA_EXAMPLE_1", ex1)
        intent.putExtra("EXTRA_EXAMPLE_2", ex2)
        intent.putExtra("EXTRA_EXAMPLE_3", ex3)
        intent.putExtra("EXTRA_MISTAKE", mistake)
        intent.putExtra("EXTRA_COLOR", colorHex)
        intent.putExtra("EXTRA_ICON", iconRes)
        startActivity(intent)
    }

    private fun setupBottomNavigation() {
        findViewById<LinearLayout>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
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
}
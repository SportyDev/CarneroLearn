package com.sportydev.carnerolearnrenewed.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.local.AdminBd
import com.sportydev.carnerolearnrenewed.data.model.GrammarTopic
import com.sportydev.carnerolearnrenewed.data.model.Word
import com.sportydev.carnerolearnrenewed.ui.reading.ReadingActivity
import com.sportydev.carnerolearnrenewed.ui.grammar.StudyBookActivity
import com.sportydev.carnerolearnrenewed.ui.grammar.TopicDetail
import com.sportydev.carnerolearnrenewed.ui.vocabulary.VocabularyActivity
import com.sportydev.carnerolearnrenewed.ui.verbs.VerbsQuizActivity
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity
import com.sportydev.carnerolearnrenewed.ui.quiz.QuizActivity
import com.sportydev.carnerolearnrenewed.ui.verbs.VerbsActivity
import com.sportydev.carnerolearnrenewed.utils.TtsManager
import java.util.Calendar
import java.util.Locale

class MainActivity : BaseActivity() {

    private lateinit var db: AdminBd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        db = AdminBd(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        TtsManager.initialize(this)

        // 1. Actualizar Racha antes de cargar datos (Asegúrate de tener la función en AdminBd)
        db.updateStreak()

        setupBottomNavigation()
        setupHeaderButtons()
        setupQuickPractice()

        // 2. Cargar datos del usuario (XP y Streak actualizados)
        loadUserData()

        // 3. Cargar contenido dinámico del día
        setupDailyContent()
    }

    private fun loadUserData() {
        val user = db.getUserData()
        if (user != null) {
            findViewById<TextView>(R.id.tvStreakValue).text = "${user.streakDays} Days"
            findViewById<TextView>(R.id.tvXpValue).text = "${user.xp} XP"
        }
    }

    private fun getDailySeed(): Int {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        return year * 1000 + dayOfYear
    }

    private fun setupDailyContent() {
        val seed = getDailySeed()

        // Configurar Lección del Día
        val topics = db.getAllGrammarTopics()
        if (topics.isNotEmpty()) {
            val dailyTopic = topics[seed % topics.size]
            updateNextLessonUI(dailyTopic)
        }

        // Configurar Palabra del Día
        val words = db.getAllWords()
        if (words.isNotEmpty()) {
            val dailyWord = words[seed % words.size]
            updateWordOfTheDayUI(dailyWord)
        }
    }

    private fun updateNextLessonUI(topic: GrammarTopic) {
        findViewById<TextView>(R.id.tvLessonName).text = topic.title
        findViewById<TextView>(R.id.tvLessonTag).text = "GRAMMAR"
        findViewById<TextView>(R.id.tvLessonLevel).text = topic.level

        findViewById<MaterialCardView>(R.id.cardNextLesson).setOnClickListener {
            val intent = Intent(this, TopicDetail::class.java).apply {
                // PASAR EL ID para que TopicDetail busque las preguntas del quiz
                putExtra("EXTRA_TOPIC_ID", topic.id)

                putExtra("EXTRA_TITLE", topic.title)
                putExtra("EXTRA_STRUCTURE", topic.structureFormula)
                putExtra("EXTRA_CONTENT", topic.explanation)

                // LIMPIAR LOS EJEMPLOS (.trim()) para que no se amontonen con espacios basura
                val examplesList = topic.examples?.split("|")?.map { it.trim() } ?: listOf()
                putExtra("EXTRA_EXAMPLE_1", examplesList.getOrNull(0) ?: "")
                putExtra("EXTRA_EXAMPLE_2", examplesList.getOrNull(1) ?: "")
                putExtra("EXTRA_EXAMPLE_3", examplesList.getOrNull(2) ?: "")

                putExtra("EXTRA_COLOR", "#2196F3")
                putExtra("EXTRA_ICON", R.drawable.ic_education)
            }
            startActivity(intent)
        }

        findViewById<FloatingActionButton>(R.id.btnStartLesson).setOnClickListener {
            findViewById<MaterialCardView>(R.id.cardNextLesson).performClick()
        }
    }

    private fun updateWordOfTheDayUI(word: Word) {
        findViewById<TextView>(R.id.tvWodEnglish).text = word.word
        findViewById<TextView>(R.id.tvWodType).text = word.type?.lowercase(Locale.ROOT)
        findViewById<TextView>(R.id.tvWodPhonetic).text = word.phonetic
        findViewById<TextView>(R.id.tvWodMeaning).text = word.translation
        findViewById<TextView>(R.id.tvWodExample).text = word.exampleSentence

        findViewById<ImageButton>(R.id.btnWodListen).setOnClickListener {
            TtsManager.speak(word.word)
            Toast.makeText(this, "Pronouncing: ${word.word}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupHeaderButtons() {
        findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            try {
                startActivity(Intent(this, SettingsActivity::class.java))
            } catch (e: Exception) {
                Toast.makeText(this, "Settings not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupQuickPractice() {
        findViewById<MaterialCardView>(R.id.btnQuickVerbs).setOnClickListener {
            startActivity(Intent(this, VerbsQuizActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<MaterialCardView>(R.id.btnQuickQuiz).setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java).apply {
                // Le indicamos a QuizActivity que cargue un mix aleatorio
                putExtra("QUIZ_MODE", "DAILY_MIX")
            }
            startActivity(intent)
            overridePendingTransition(0, 0)
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
        findViewById<LinearLayout>(R.id.nav_verbs).setOnClickListener {
            startActivity(Intent(this, VerbsActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.nav_reading).setOnClickListener {
            startActivity(Intent(this, ReadingActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }

    override fun onDestroy() {
        TtsManager.stop()
        super.onDestroy()
    }
}
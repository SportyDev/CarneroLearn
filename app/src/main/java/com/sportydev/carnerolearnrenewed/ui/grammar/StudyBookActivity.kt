package com.sportydev.carnerolearnrenewed.ui.grammar

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.local.AdminBd
import com.sportydev.carnerolearnrenewed.data.model.GrammarTopic
import com.sportydev.carnerolearnrenewed.ui.adapters.GrammarAdapter
import com.sportydev.carnerolearnrenewed.ui.reading.ReadingActivity
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity
import com.sportydev.carnerolearnrenewed.ui.verbs.VerbsActivity
import com.sportydev.carnerolearnrenewed.ui.main.MainActivity
import com.sportydev.carnerolearnrenewed.ui.vocabulary.VocabularyActivity

class StudyBookActivity : BaseActivity() {

    private lateinit var adminBd: AdminBd
    private lateinit var adapter: GrammarAdapter
    private var allTopics: List<GrammarTopic> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_study_book)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adminBd = AdminBd(this)
        setupRecyclerView()
        loadTopics()
        setupSearch()
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvTopics)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // CUIDADO AQUÍ: Actualizamos la forma en la que escuchamos el clic para recibir
        // el tema, el color y el icono directamente desde el GrammarAdapter
        adapter = GrammarAdapter(emptyList()) { topic, colorHex, iconRes ->
            openTopic(topic, colorHex, iconRes)
        }

        recyclerView.adapter = adapter
    }

    private fun loadTopics() {
        allTopics = adminBd.getAllGrammarTopics()
        adapter.updateList(allTopics)
    }

    private fun setupSearch() {
        val etSearch = findViewById<EditText>(R.id.etSearch)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterTopics(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterTopics(query: String) {
        val filteredList = allTopics.filter {
            it.title.contains(query, ignoreCase = true) ||
                    (it.level?.contains(query, ignoreCase = true) == true)
        }
        adapter.updateList(filteredList)
    }

    private fun openTopic(topic: GrammarTopic, colorHex: String, iconRes: Int) {
        val intent = Intent(this, TopicDetail::class.java)

        // --- LA LÍNEA MÁS IMPORTANTE PARA EL QUIZ DINÁMICO ---
        intent.putExtra("EXTRA_ID", topic.id)
        // -----------------------------------------------------

        intent.putExtra("EXTRA_TITLE", topic.title)
        intent.putExtra("EXTRA_STRUCTURE", topic.structureFormula)
        intent.putExtra("EXTRA_CONTENT", topic.explanation)

        // 1. Tomamos el string (si es null, usamos string vacío)
        val rawExamples = topic.examples ?: ""

        // 2. Reemplazamos el literal "\\n" por un salto de línea "\n" real y luego dividimos
        val examplesList = rawExamples.replace("\\n", "\n")
            .split("\n")
            .map { it.trim() } // Limpiamos espacios en blanco al inicio y final
            .filter { it.isNotEmpty() } // Filtramos los que queden vacíos

        // 3. Asignamos de forma segura (si no existe el índice, manda string vacío)
        intent.putExtra("EXTRA_EXAMPLE_1", examplesList.getOrNull(0) ?: "")
        intent.putExtra("EXTRA_EXAMPLE_2", examplesList.getOrNull(1) ?: "")
        intent.putExtra("EXTRA_EXAMPLE_3", examplesList.getOrNull(2) ?: "")

        intent.putExtra(
            "EXTRA_MISTAKE",
            "Remember to practice the structure."
        ) // Genérico o extraer de BD si existiera

        // Usamos los colores e iconos que calculó el Adapter para que haya consistencia visual
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
            startActivity(Intent(this, VerbsActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.nav_reading).setOnClickListener {
            startActivity(Intent(this, ReadingActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }
}
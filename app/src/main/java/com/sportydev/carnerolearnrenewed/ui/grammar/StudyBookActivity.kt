package com.sportydev.carnerolearnrenewed.ui.grammar

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.local.AdminBd
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity
import com.sportydev.carnerolearnrenewed.ui.listening.ListeningActivity
import com.sportydev.carnerolearnrenewed.ui.main.MainActivity
import com.sportydev.carnerolearnrenewed.ui.reading.ReadingActivity
import com.sportydev.carnerolearnrenewed.ui.vocabulary.VocabularyActivity

class StudyBookActivity : BaseActivity() {
    // RecyclerView = componente que muestra una lista con scroll
    private lateinit var recyclerView: RecyclerView
    // Adapter = el puente que conecta los datos con la lista visual
    private lateinit var adapter: GrammarTopAdapter
    //Acceso a la base de datos
    private lateinit var adminBd: AdminBd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_study_book)

        // Conexión con la base de datos
        adminBd = AdminBd(this)

        recyclerView = findViewById(R.id.rvGrammarTopics)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadGrammarTopics()

      // setupBottomNavigation()
    }


     //función se encarga de:
     //        1 Pedir los temas a la base de datos
     //        2 Crear el adapter
     //        3 Mostrar la lista en pantalla
    private fun loadGrammarTopics() {
          //Obtenemos todos los temas en la base de datos
        val topics = adminBd.getAllGrammarTopics()  // Metodo en AdminBd

        adapter = GrammarTopAdapter(topics) { topic ->

            val intent = Intent(this, TopicDetail::class.java)
            // Enviamos el ID del tema a la siguiente pantalla
            intent.putExtra("TOPIC_ID", topic.id)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }

    //--------------------REVISAR------------------------------------------

   /* private fun setupBottomNavigation() {
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
    }*/
}
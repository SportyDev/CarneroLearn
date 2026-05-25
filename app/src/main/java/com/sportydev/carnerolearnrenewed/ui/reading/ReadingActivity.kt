package com.sportydev.carnerolearnrenewed.ui.reading

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.local.AdminBd
import com.sportydev.carnerolearnrenewed.ui.adapters.ReadingAdapter
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity
import com.sportydev.carnerolearnrenewed.ui.grammar.StudyBookActivity
import com.sportydev.carnerolearnrenewed.ui.verbs.VerbsActivity
import com.sportydev.carnerolearnrenewed.ui.main.MainActivity
import com.sportydev.carnerolearnrenewed.ui.vocabulary.VocabularyActivity

class ReadingActivity : BaseActivity() {

    private lateinit var adminBd: AdminBd
    private lateinit var adapter: ReadingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reading)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adminBd = AdminBd(this)
        setupRecyclerView()
        setupSearch()
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {
        val rvStories = findViewById<RecyclerView>(R.id.rvStories)
        val stories = adminBd.getAllReadingResources()
        
        adapter = ReadingAdapter(stories)
        rvStories.layoutManager = LinearLayoutManager(this)
        rvStories.adapter = adapter
    }

    private fun setupSearch() {
        val etSearch = findViewById<TextInputEditText>(R.id.etSearchReading)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun setupBottomNavigation() {
        findViewById<LinearLayout>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.nav_study).setOnClickListener {
            startActivity(Intent(this, StudyBookActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.nav_listening).setOnClickListener {
            startActivity(Intent(this, VerbsActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.nav_vocabulary).setOnClickListener {
            startActivity(Intent(this, VocabularyActivity::class.java))
            finish()
        }

    }

}
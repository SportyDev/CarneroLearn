package com.sportydev.carnerolearnrenewed.ui.verbs

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.local.AdminBd
import com.sportydev.carnerolearnrenewed.ui.adapters.VerbLibraryAdapter
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity

class VerbLibraryActivity : BaseActivity() {

    private lateinit var adminBd: AdminBd
    private lateinit var rvVerbs: RecyclerView
    private lateinit var adapter: VerbLibraryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_verb_library)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rvVerbs)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        adminBd = AdminBd(this)

        // Configurar botón de atrás
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Configurar el RecyclerView
        rvVerbs = findViewById(R.id.rvVerbs)
        rvVerbs.layoutManager = LinearLayoutManager(this)

        // Obtener datos y pasarlos al adapter
        val verbsList = adminBd.getAllIrregularVerbs()
        adapter = VerbLibraryAdapter(verbsList)
        rvVerbs.adapter = adapter
    }
}
package com.sportydev.carnerolearnrenewed.ui.vocabulary

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.sportydev.carnerolearnrenewed.ui.vocabulary.CategoryDetailActivity
import com.sportydev.carnerolearnrenewed.ui.listening.ListeningActivity
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.ui.reading.ReadingActivity
import com.sportydev.carnerolearnrenewed.ui.grammar.StudyBookActivity
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity
import com.sportydev.carnerolearnrenewed.ui.main.MainActivity

class VocabularyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vocabulary)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupCategoryClicks()
        setupBottomNavigation()
    }

    private fun setupCategoryClicks() {

        // 1. Airport (Azul)
        findViewById<MaterialCardView>(R.id.cardAirport)?.setOnClickListener {
            openCategory("Airport", "#4A90E2")
        }

        // 2. Hotel (Naranja)
        findViewById<MaterialCardView>(R.id.cardHotel)?.setOnClickListener {
            openCategory("Hotel", "#F5A623")
        }

        // 3. Restaurant (Rosa/Rojo)
        findViewById<MaterialCardView>(R.id.cardRestaurant)?.setOnClickListener {
            openCategory("Restaurant", "#E91E63")
        }

        // 4. City (Morado)
        findViewById<MaterialCardView>(R.id.cardCity)?.setOnClickListener {
            openCategory("City", "#9C27B0")
        }

        // 5. Transportation (Verde azulado)
        findViewById<MaterialCardView>(R.id.cardTransport)?.setOnClickListener {
            openCategory("Transportation", "#009688")
        }

        // 6. Shopping (Naranja fuerte)
        findViewById<MaterialCardView>(R.id.cardShopping)?.setOnClickListener {
            openCategory("Shopping", "#FF5722")
        }

        // 7. Technology (Azul oscuro/Indigo)
        findViewById<MaterialCardView>(R.id.cardTech)?.setOnClickListener {
            openCategory("Technology", "#3F51B5")
        }

        // 8. Sports (Verde lima)
        findViewById<MaterialCardView>(R.id.cardSports)?.setOnClickListener {
            openCategory("Sports", "#8BC34A")
        }

        // falta  Health, Education
    }

    private fun openCategory(name: String, colorHex: String) {
        val intent = Intent(this, CategoryDetailActivity::class.java)
        intent.putExtra("CATEGORY_NAME", name)
        intent.putExtra("CATEGORY_COLOR", colorHex)
        startActivity(intent)
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
            startActivity(Intent(this, ListeningActivity::class.java))
            overridePendingTransition(0, 0)
        }

        findViewById<LinearLayout>(R.id.nav_reading).setOnClickListener {
            startActivity(Intent(this, ReadingActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }
}
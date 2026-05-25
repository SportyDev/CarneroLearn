package com.sportydev.carnerolearnrenewed.ui.verbs

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.local.AdminBd
import com.sportydev.carnerolearnrenewed.ui.reading.ReadingActivity
import com.sportydev.carnerolearnrenewed.ui.grammar.StudyBookActivity
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity
import com.sportydev.carnerolearnrenewed.ui.main.MainActivity
import com.sportydev.carnerolearnrenewed.ui.vocabulary.VocabularyActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.max

class VerbsActivity : BaseActivity() {

    private lateinit var adminBd: AdminBd
    private lateinit var tvMasteredCount: TextView
    private lateinit var tvDayStreak: TextView
    private lateinit var bars: List<MaterialCardView> // Ahora son MaterialCardView
    private lateinit var tvScores: List<TextView>
    private lateinit var tvLabels: List<TextView> // Las nuevas etiquetas de fecha


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_verbs)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adminBd = AdminBd(this)

        initViews()
        setupBottomNavigation()

        // Configurar el clic para iniciar el juego
        findViewById<MaterialCardView>(R.id.btnStartGame).setOnClickListener {
            startActivity(Intent(this, VerbsQuizActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        findViewById<MaterialButton>(R.id.btnVerbLibrary).setOnClickListener {
            startActivity(Intent(this, VerbLibraryActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun initViews() {
        tvMasteredCount = findViewById(R.id.tvMasteredCount)
        tvDayStreak = findViewById(R.id.tvDayStreak)

        bars = listOf(
            findViewById(R.id.bar1), findViewById(R.id.bar2),
            findViewById(R.id.bar3), findViewById(R.id.bar4), findViewById(R.id.bar5)
        )

        tvScores = listOf(
            findViewById(R.id.tvScore1), findViewById(R.id.tvScore2),
            findViewById(R.id.tvScore3), findViewById(R.id.tvScore4), findViewById(R.id.tvScore5)
        )

        tvLabels = listOf(
            findViewById(R.id.tvLabel1), findViewById(R.id.tvLabel2),
            findViewById(R.id.tvLabel3), findViewById(R.id.tvLabel4), findViewById(R.id.tvLabel5)
        )
    }

    // onResume se ejecuta cada vez que volvemos a esta pantalla
    override fun onResume() {
        super.onResume()
        loadStatistics()
    }

    private fun loadStatistics() {
        // 1. Cargar Mastered
        val totalMastered = adminBd.getTotalMasteredVerbs()
        tvMasteredCount.text = totalMastered.toString()

        // 2. Calcular Racha de Días
        val streak = calculateDayStreak(adminBd.getPlayedDates())
        tvDayStreak.text = streak.toString()

        // 3. Cargar Gráfica (Últimas 5 partidas)
        val lastGames = adminBd.getLastFiveDaysResults()
        val maxBarHeightDp = 100

        // Configuración de formatos de fecha
        val dbDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dayOfWeekFormat =
            SimpleDateFormat("EEE", Locale.getDefault()) // Retorna "lun", "mar", etc.

        val todayStr = dbDateFormat.format(Date())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayStr = dbDateFormat.format(calendar.time)

        // Limpiar todas las barras a estado "Fantasma" (Empty State)
        for (i in bars.indices) {
            val layoutParams = bars[i].layoutParams
            layoutParams.height = dpToPx(100) // Altura completa fantasma
            bars[i].layoutParams = layoutParams
            bars[i].setCardBackgroundColor(android.graphics.Color.parseColor("#F5F5F5")) // Gris ultra claro
            tvScores[i].text = "-"
            tvScores[i].setTextColor(android.graphics.Color.parseColor("#BDBDBD"))
            tvLabels[i].text = "---"
            tvLabels[i].setTextColor(android.graphics.Color.parseColor("#BDBDBD"))
        }

        // Pintar los datos reales
        for (i in lastGames.indices) {
            if (i < bars.size) {
                val game = lastGames[i]
                val bar = bars[i]
                val tvScore = tvScores[i]
                val tvLabel = tvLabels[i]

                // Porcentaje y Altura
                val percentage = if (game.total > 0) game.score.toFloat() / game.total else 0f
                val heightInDp = (maxBarHeightDp * percentage).toInt()

                val layoutParams = bar.layoutParams
                layoutParams.height = max(
                    dpToPx(heightInDp),
                    dpToPx(16)
                ) // Min 16dp para que siempre se vea redondita
                bar.layoutParams = layoutParams

                // Textos
                tvScore.text = game.score.toString()

                // Lógica de Etiquetas (Fechas)
                try {
                    val gameDateStr = game.datePlayed
                    when (gameDateStr) {
                        todayStr -> tvLabel.text = "Hoy"
                        yesterdayStr -> tvLabel.text = "Ayer"
                        else -> {
                            val parsedDate = dbDateFormat.parse(gameDateStr)
                            // Capitalizamos la primera letra del día (ej. "Mie")
                            val dayStr = dayOfWeekFormat.format(parsedDate!!).take(3).capitalize()
                            tvLabel.text = dayStr
                        }
                    }
                } catch (e: Exception) {
                    tvLabel.text = "Doc"
                }

                // Lógica de Colores Dinámicos (Semáforo)
                if (percentage >= 0.8f) {
                    // Excelente (Verde)
                    bar.setCardBackgroundColor(android.graphics.Color.parseColor("#66BB6A"))
                    tvScore.setTextColor(android.graphics.Color.parseColor("#43A047"))
                    tvLabel.setTextColor(android.graphics.Color.parseColor("#43A047"))
                } else if (percentage >= 0.5f) {
                    // Regular (Naranja)
                    bar.setCardBackgroundColor(android.graphics.Color.parseColor("#FFA726"))
                    tvScore.setTextColor(android.graphics.Color.parseColor("#F57C00"))
                    tvLabel.setTextColor(android.graphics.Color.parseColor("#F57C00"))
                } else {
                    // Reprobado (Rojo Suave)
                    bar.setCardBackgroundColor(android.graphics.Color.parseColor("#EF5350"))
                    tvScore.setTextColor(android.graphics.Color.parseColor("#E53935"))
                    tvLabel.setTextColor(android.graphics.Color.parseColor("#E53935"))
                }
            }
        }
    }

    private fun calculateDayStreak(dates: List<String>): Int {
        if (dates.isEmpty()) return 0

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayStr = dateFormat.format(Date())

        var currentStreak = 0
        val calendar = Calendar.getInstance()

        // Revisa si jugó hoy
        if (dates[0] == todayStr) {
            currentStreak = 1
        } else {
            // Revisa si jugó ayer (si no jugó hoy ni ayer, la racha es 0)
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterdayStr = dateFormat.format(calendar.time)

            if (dates[0] == yesterdayStr) {
                currentStreak = 1
            } else {
                return 0
            }
        }

        // Si hay una racha activa, contar cuántos días consecutivos hacia atrás existen
        var checkDate = dateFormat.parse(dates[0])
        val checkCalendar = Calendar.getInstance()

        for (i in 1 until dates.size) {
            checkCalendar.time = checkDate!!
            checkCalendar.add(Calendar.DAY_OF_YEAR, -1) // Un día antes
            val expectedPreviousDateStr = dateFormat.format(checkCalendar.time)

            if (dates[i] == expectedPreviousDateStr) {
                currentStreak++
                checkDate = checkCalendar.time // Avanzamos la fecha a revisar
            } else {
                break // Se rompió la racha
            }
        }

        return currentStreak
    }

    // Función auxiliar para convertir DP a Pixeles de pantalla
    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
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
        findViewById<LinearLayout>(R.id.nav_reading).setOnClickListener {
            startActivity(Intent(this, ReadingActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.nav_vocabulary).setOnClickListener {
            startActivity(Intent(this, VocabularyActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

    }
}
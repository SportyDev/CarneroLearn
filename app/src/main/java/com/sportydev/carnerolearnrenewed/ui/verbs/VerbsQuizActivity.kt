package com.sportydev.carnerolearnrenewed.ui.verbs

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.local.AdminBd
import com.sportydev.carnerolearnrenewed.data.model.IrregularVerb
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity

class VerbsQuizActivity : BaseActivity() {

    private lateinit var adminBd: AdminBd

    // UI Elements
    private lateinit var layoutGame: ConstraintLayout
    private lateinit var layoutSummary: LinearLayout
    private lateinit var btnClose: ImageView
    private lateinit var quizProgress: LinearProgressIndicator
    private lateinit var tvQuestionCounter: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvSpanishWord: TextView
    private lateinit var slotBaseForm: TextView
    private lateinit var slotPastSimple: TextView
    private lateinit var slotPastParticiple: TextView
    private lateinit var optionsButtons: List<Button>
    private lateinit var btnContinueQuestion: Button

    // Summary UI
    private lateinit var tvFinalScore: TextView
    private lateinit var btnFinishQuiz: Button
    private lateinit var cardOptions: MaterialCardView
    private lateinit var llMistakesList: LinearLayout
    private lateinit var tvReviewTitle: TextView

    private var currentVerb: IrregularVerb? = null

    // Controladores de juego
    private val expectedAnswers = mutableListOf<String>()
    private val slotsFilled = BooleanArray(3)
    private var filledSlotsCount = 0
    private var selectedOptionBtn: Button? = null // ¡NUEVO! Guarda qué botón se tocó primero

    private val totalQuestions = 10
    private var currentQuestionIndex = 1
    private var score = 0
    private var mistakesInCurrentQuestion = 0

    // Para el resumen final
    private val failedVerbs = mutableSetOf<IrregularVerb>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_verbs_quiz)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adminBd = AdminBd(this)
        initViews()
        setupListeners()

        quizProgress.max = totalQuestions
        loadNextQuestion()
    }

    private fun initViews() {
        layoutGame = findViewById(R.id.layoutGame)
        layoutSummary = findViewById(R.id.layoutSummary)
        btnClose = findViewById(R.id.btnClose)
        quizProgress = findViewById(R.id.quizProgress)
        tvQuestionCounter = findViewById(R.id.tvQuestionCounter)
        tvScore = findViewById(R.id.tvScore)
        tvSpanishWord = findViewById(R.id.tvSpanishWord)
        slotBaseForm = findViewById(R.id.slotBaseForm)
        slotPastSimple = findViewById(R.id.slotPastSimple)
        slotPastParticiple = findViewById(R.id.slotPastParticiple)
        cardOptions = findViewById(R.id.cardOptions)
        btnContinueQuestion = findViewById(R.id.btnContinueQuestion)

        tvFinalScore = findViewById(R.id.tvFinalScore)
        btnFinishQuiz = findViewById(R.id.btnFinishQuiz)
        llMistakesList = findViewById(R.id.llMistakesList)
        tvReviewTitle = findViewById(R.id.tvReviewTitle)

        optionsButtons = listOf(
            findViewById(R.id.btnOpt1), findViewById(R.id.btnOpt2),
            findViewById(R.id.btnOpt3), findViewById(R.id.btnOpt4),
            findViewById(R.id.btnOpt5), findViewById(R.id.btnOpt6)
        )
    }

    private fun setupListeners() {
        btnClose.setOnClickListener { finish() }
        btnContinueQuestion.setOnClickListener { moveToNextQuestion() }
        btnFinishQuiz.setOnClickListener { finish() }

        // Paso 1: Tocar la opción
        for (button in optionsButtons) {
            button.setOnClickListener { handleOptionSelected(it as Button) }
        }

        // Paso 2: Tocar el hueco
        slotBaseForm.setOnClickListener { handleSlotTapped(0, it as TextView) }
        slotPastSimple.setOnClickListener { handleSlotTapped(1, it as TextView) }
        slotPastParticiple.setOnClickListener { handleSlotTapped(2, it as TextView) }
    }

    private fun loadNextQuestion() {
        mistakesInCurrentQuestion = 0
        filledSlotsCount = 0
        slotsFilled.fill(false)
        expectedAnswers.clear()
        selectedOptionBtn = null

        tvQuestionCounter.text = "$currentQuestionIndex / $totalQuestions"
        tvScore.text = "Score: $score"
        quizProgress.progress = currentQuestionIndex
        btnContinueQuestion.visibility = View.GONE
        cardOptions.alpha = 1.0f

        val emptyDashedBg = R.drawable.bg_slot_dashed
        val slots = listOf(slotBaseForm, slotPastSimple, slotPastParticiple)
        for (slot in slots) {
            slot.text = ""
            slot.setBackgroundResource(emptyDashedBg)
            slot.setTextColor(Color.parseColor("#311B92"))
        }

        currentVerb = adminBd.getRandomVerb()
        if (currentVerb == null) {
            finish(); return
        }

        tvSpanishWord.text = currentVerb!!.translation.uppercase()

        expectedAnswers.add(currentVerb!!.baseForm)
        expectedAnswers.add(currentVerb!!.pastSimple)
        expectedAnswers.add(currentVerb!!.pastParticiple)

        generateOptions()
        resetOptionButtons()
    }

    private fun generateOptions() {
        val optionsList = mutableListOf<String>()
        optionsList.addAll(expectedAnswers)

        // DISTRACTORES DIABÓLICOS: Buscamos verbos parecidos
        val similarVerbs = adminBd.getRandomVerbsByPattern(currentVerb!!.patternGroup ?: "", 3)

        for (verb in similarVerbs) {
            if (optionsList.size >= 6) break
            if (verb.id != currentVerb!!.id) {
                // Tomamos una forma aleatoria de ese verbo parecido
                val distractor =
                    listOf(verb.baseForm, verb.pastSimple, verb.pastParticiple).random()
                if (!optionsList.contains(distractor)) optionsList.add(distractor)
            }
        }

        // Si faltan huecos, metemos aleatorios normales
        while (optionsList.size < 6) {
            val randomVerb = adminBd.getRandomVerb()
            if (randomVerb != null && randomVerb.id != currentVerb!!.id) {
                val distractor = listOf(
                    randomVerb.baseForm,
                    randomVerb.pastSimple,
                    randomVerb.pastParticiple
                ).random()
                if (!optionsList.contains(distractor)) optionsList.add(distractor)
            }
        }

        optionsList.shuffle()
        for (i in optionsButtons.indices) {
            optionsButtons[i].text = optionsList[i]
        }
    }

    private fun resetOptionButtons() {
        for (button in optionsButtons) {
            button.isEnabled = true
            button.alpha = 1.0f
            deselectButtonVisual(button)
        }
    }

    // --- LÓGICA DE 2 PASOS ---

    private fun handleOptionSelected(button: Button) {
        // Deseleccionamos el anterior si había uno
        selectedOptionBtn?.let { deselectButtonVisual(it) }

        // Seleccionamos el nuevo
        selectedOptionBtn = button

        // Visualmente le damos un toque "activo" (Amarillo pastel)
        button.setBackgroundColor(Color.parseColor("#FFF59D"))
    }

    private fun deselectButtonVisual(button: Button) {
        button.setBackgroundColor(Color.parseColor("#EDE7F6"))
        button.setTextColor(Color.parseColor("#311B92"))
    }

    private fun handleSlotTapped(slotIndex: Int, slotView: TextView) {
        // Si el hueco ya se llenó, lo ignoramos
        if (slotsFilled[slotIndex]) return

        // Si no ha seleccionado ninguna palabra, le avisamos
        if (selectedOptionBtn == null) {
            Toast.makeText(this, "Selecciona una palabra primero", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedAnswer = selectedOptionBtn!!.text.toString()

        // ¿Es la respuesta correcta para ESTE hueco?
        if (expectedAnswers[slotIndex] == selectedAnswer) {
            // ACIERTO
            slotsFilled[slotIndex] = true
            filledSlotsCount++

            slotView.text = selectedAnswer
            slotView.setBackgroundColor(Color.parseColor("#E8F5E9")) // Verde
            slotView.setTextColor(Color.parseColor("#2E7D32"))

            // Deshabilitar la opción usada
            selectedOptionBtn!!.isEnabled = false
            selectedOptionBtn!!.alpha = 0.3f
            deselectButtonVisual(selectedOptionBtn!!)
            selectedOptionBtn = null // Limpiar selección

            // ¿Ganó la pregunta?
            if (filledSlotsCount == 3) {
                score++
                tvScore.text = "Score: $score"
                setAllButtonsEnabled(false)
                Handler(Looper.getMainLooper()).postDelayed({ moveToNextQuestion() }, 1000)
            }
        } else {
            // ERROR
            mistakesInCurrentQuestion++
            failedVerbs.add(currentVerb!!) // Guardamos el error para el Resumen

            slotView.setBackgroundColor(Color.parseColor("#FFCDD2")) // Hueco en rojo temporal

            Handler(Looper.getMainLooper()).postDelayed({
                if (!slotsFilled[slotIndex]) slotView.setBackgroundResource(R.drawable.bg_slot_dashed)
            }, 500)

            // Deseleccionamos el botón
            deselectButtonVisual(selectedOptionBtn!!)
            selectedOptionBtn = null

            if (mistakesInCurrentQuestion >= 2) {
                failQuestion() // Superó el límite de errores
            } else {
                Toast.makeText(this, "Intenta de nuevo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun failQuestion() {
        setAllButtonsEnabled(false)
        cardOptions.alpha = 0.5f

        val slots = listOf(slotBaseForm, slotPastSimple, slotPastParticiple)

        // AUTOCOMPLETAR
        for (i in 0..2) {
            if (!slotsFilled[i]) {
                slots[i].text = expectedAnswers[i]
                slots[i].setBackgroundColor(Color.parseColor("#FFCDD2"))
                slots[i].setTextColor(Color.parseColor("#C62828"))
            }
        }

        btnContinueQuestion.visibility = View.VISIBLE
    }

    private fun moveToNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex <= totalQuestions) {
            loadNextQuestion()
        } else {
            showSummary()
        }
    }

    private fun setAllButtonsEnabled(enabled: Boolean) {
        for (button in optionsButtons) {
            button.isEnabled = enabled
        }
    }

    // --- RESUMEN FINAL ---

    private fun showSummary() {
        layoutGame.visibility = View.GONE
        layoutSummary.visibility = View.VISIBLE

        tvFinalScore.text = "Acertaste $score de $totalQuestions verbos."

        // Generar la lista de errores dinámica
        if (failedVerbs.isNotEmpty()) {
            tvReviewTitle.visibility = View.VISIBLE

            for (verb in failedVerbs) {
                val tvMistake = TextView(this)
                tvMistake.text =
                    "${verb.translation.uppercase()}:\n${verb.baseForm} - ${verb.pastSimple} - ${verb.pastParticiple}"
                tvMistake.setTextColor(Color.WHITE)
                tvMistake.textSize = 16f
                tvMistake.setPadding(0, 16, 0, 16)
                tvMistake.gravity = Gravity.CENTER

                // Agregar línea separadora
                val divider = View(this)
                divider.layoutParams =
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)
                divider.setBackgroundColor(Color.parseColor("#5E35B1"))

                llMistakesList.addView(tvMistake)
                llMistakesList.addView(divider)
            }
        }

        adminBd.insertQuizResult(score, totalQuestions)
    }
}
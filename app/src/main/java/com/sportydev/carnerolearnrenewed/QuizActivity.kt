package com.sportydev.carnerolearnrenewed

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator

class QuizActivity : AppCompatActivity() {

    // --- VARIABLES DE LÓGICA ---
    private var questionsList: List<QuizQuestion> = listOf()
    private var currentQuestionIndex = 0
    private var score = 0
    private var isAnswered = false
    private var currentTopic = ""

    // --- REFERENCIAS DE UI ---
    // Contenedores
    private lateinit var layoutQuizContent: ConstraintLayout
    private lateinit var layoutResults: LinearLayout

    // Elementos del Quiz
    private lateinit var tvTopicBadge: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var optionsButtons: List<MaterialButton>

    // Elementos de Resultados
    private lateinit var tvFinalScore: TextView
    private lateinit var tvResultMessage: TextView
    private lateinit var lottieConfetti: LottieAnimationView
    private lateinit var lottieCorrectBurst: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // 1. Inicializar Audio
        SoundManager.initialize(this)

        // 2. Conectar Vistas
        initViews()

        // 3. Configurar Botones de Navegación
        findViewById<ImageButton>(R.id.btnClose).setOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.btnFinish).setOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.btnRestart).setOnClickListener { restartQuiz() }

        // 4. Obtener el tema
        currentTopic = intent.getStringExtra("QUIZ_TOPIC") ?: "General"
        tvTopicBadge.text = currentTopic.uppercase()

        // 5. Iniciar
        startQuiz()
    }

    private fun initViews() {
        layoutQuizContent = findViewById(R.id.layoutQuizContent)
        layoutResults = findViewById(R.id.layoutResults)
        lottieCorrectBurst = findViewById(R.id.lottieCorrectBurst)

        tvTopicBadge = findViewById(R.id.tvTopicBadge)
        tvQuestion = findViewById(R.id.tvQuestion)
        progressBar = findViewById(R.id.progressBarQuiz)

        tvFinalScore = findViewById(R.id.tvFinalScore)
        tvResultMessage = findViewById(R.id.tvResultMessage)
        lottieConfetti = findViewById(R.id.lottieConfetti)

        optionsButtons = listOf(
            findViewById(R.id.btnOption1),
            findViewById(R.id.btnOption2),
            findViewById(R.id.btnOption3),
            findViewById(R.id.btnOption4)
        )
    }

    // --- FLUJO DEL JUEGO ---

    private fun startQuiz() {
        score = 0
        currentQuestionIndex = 0
        questionsList = getQuestionsForTopic(currentTopic)

        if (questionsList.isNotEmpty()) {
            layoutResults.visibility = View.GONE
            layoutQuizContent.visibility = View.VISIBLE
            setQuestion()
        } else {
            Toast.makeText(this, "No questions found for: $currentTopic", Toast.LENGTH_SHORT).show()
            finish() // Si no hay preguntas, cerramos
        }
    }

    private fun restartQuiz() {
        startQuiz()
    }

    private fun setQuestion() {
        isAnswered = false
        val question = questionsList[currentQuestionIndex]

        // Actualizar textos y barra
        tvQuestion.text = question.question
        val progress = ((currentQuestionIndex) * 100) / questionsList.size
        progressBar.setProgressCompat(progress, true)

        // Configurar botones
        optionsButtons.forEachIndexed { index, button ->
            if (index < question.options.size) {
                button.visibility = View.VISIBLE
                button.text = question.options[index]
                button.isEnabled = true

                // RESTAURAR ESTILO ORIGINAL (Outlined)
                resetButtonColor(button)

                button.setOnClickListener {
                    if (!isAnswered) {
                        checkAnswer(index)
                    }
                }
            } else {
                button.visibility = View.GONE
            }
        }
    }

    private fun checkAnswer(selectedIndex: Int) {
        isAnswered = true
        val currentQuestion = questionsList[currentQuestionIndex]

        if (selectedIndex == currentQuestion.correctAnswerIndex) {
            // === CORRECTO ===
            score++
            setButtonColor(optionsButtons[selectedIndex], true)
            SoundManager.playCorrect()

            lottieCorrectBurst.cancelAnimation() // Resetea si ya estaba sonando
            lottieCorrectBurst.progress = 0f     // Vuelve al inicio
            lottieCorrectBurst.playAnimation()   // Play
        } else {
            // === INCORRECTO ===
            setButtonColor(optionsButtons[selectedIndex], false) // Marca roja la elegida
            setButtonColor(
                optionsButtons[currentQuestion.correctAnswerIndex],
                true
            ) // Revela la correcta

            SoundManager.playWrong()
            shakeButton(optionsButtons[selectedIndex]) // Animación de error
        }

        // Delay para la siguiente pregunta
        Handler(Looper.getMainLooper()).postDelayed({
            if (currentQuestionIndex < questionsList.size - 1) {
                currentQuestionIndex++
                setQuestion()
            } else {
                showResultsScreen()
            }
        }, 1500)
    }

    private fun showResultsScreen() {
        // Cambiar vistas
        layoutQuizContent.visibility = View.GONE
        layoutResults.visibility = View.VISIBLE

        tvFinalScore.text = "You scored $score / ${questionsList.size}"

        // Lógica de Ganar/Perder (50%)
        val passed = score >= (questionsList.size / 2)

        if (passed) {
            tvResultMessage.text = "Great job! Keep learning."
            tvFinalScore.setTextColor(Color.parseColor("#4CAF50")) // Verde
            SoundManager.playWin()

            // Activar confeti
            lottieConfetti.visibility = View.VISIBLE
            lottieConfetti.repeatCount = com.airbnb.lottie.LottieDrawable.INFINITE
            lottieConfetti.playAnimation()
        } else {
            tvResultMessage.text = "Don't give up! Try again."
            tvFinalScore.setTextColor(Color.parseColor("#F44336")) // Rojo
            SoundManager.playLose()

            // Ocultar confeti
            lottieConfetti.visibility = View.GONE
        }
    }

    // --- ESTILOS VISUALES ---

    private fun setButtonColor(button: MaterialButton, isCorrect: Boolean) {
        val strokeColorHex = if (isCorrect) "#4CAF50" else "#F44336" // Borde fuerte
        val bgColorHex = if (isCorrect) "#E8F5E9" else "#FFEBEE"     // Fondo suave
        val textColorHex = if (isCorrect) "#2E7D32" else "#C62828"   // Texto oscuro
        val iconRes = if (isCorrect) R.drawable.ic_check_circle else R.drawable.ic_cancel

        button.strokeColor = ColorStateList.valueOf(Color.parseColor(strokeColorHex))
        button.strokeWidth = 6 // Borde grueso al responder

        button.setTextColor(Color.parseColor(textColorHex))

        // Fondo con color (quita la transparencia)
        button.backgroundTintList = ColorStateList.valueOf(Color.parseColor(bgColorHex))

        // Cambiar icono
        button.setIconResource(iconRes)
        button.iconTint = ColorStateList.valueOf(Color.parseColor(strokeColorHex))
    }

    private fun resetButtonColor(button: MaterialButton) {
        // Estilo Outlined original
        val defaultColor = Color.parseColor("#546E7A") // Gris azulado
        val defaultStroke = Color.parseColor("#CFD8DC") // Gris claro

        button.strokeColor = ColorStateList.valueOf(defaultStroke)
        button.strokeWidth = 2 // Borde fino

        button.setTextColor(defaultColor)

        // Fondo Transparente
        button.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)

        // Icono círculo vacío
        button.setIconResource(R.drawable.ic_radio_button_unchecked)
        button.iconTint = ColorStateList.valueOf(defaultStroke)
    }

    private fun shakeButton(view: View) {
        val shake = ObjectAnimator.ofFloat(
            view,
            "translationX",
            0f,
            25f,
            -25f,
            25f,
            -25f,
            15f,
            -15f,
            6f,
            -6f,
            0f
        )
        shake.duration = 500
        shake.start()
    }

    // --- BASE DE DATOS DE PREGUNTAS (5 POR TEMA) ---
    private fun getQuestionsForTopic(topic: String): List<QuizQuestion> {
        val list = mutableListOf<QuizQuestion>()

        when (topic) {
            // LISTENING
            "Mystery of the Ocean", "The Future of AI" -> {
                list.add(
                    QuizQuestion(
                        "How much of the ocean has been explored?",
                        listOf("Less than 5%", "70%", "50%", "100%"),
                        0
                    )
                )
                list.add(
                    QuizQuestion(
                        "Where is the deepest part?",
                        listOf("Great Reef", "Marianas Trench", "Red Sea", "Amazon River"),
                        1
                    )
                )
                list.add(
                    QuizQuestion(
                        "What makes deep sea life difficult?",
                        listOf("Fire", "Extreme pressure", "Too much light", "Plastic"),
                        1
                    )
                )
                list.add(
                    QuizQuestion(
                        "Some creatures can...",
                        listOf("Fly", "Sing opera", "Glow in the dark", "Speak English"),
                        2
                    )
                )
                list.add(QuizQuestion("The ocean covers...", listOf("30%", "10%", "70%", "90%"), 2))
            }

            // READING
            "The Lost Key" -> {
                list.add(
                    QuizQuestion(
                        "Where did Oliver start looking?",
                        listOf("Kitchen", "Library", "Garden", "Attic"),
                        1
                    )
                )
                list.add(
                    QuizQuestion(
                        "What was the weather like?",
                        listOf("Sunny", "Snowy", "Rainy", "Windy"),
                        2
                    )
                )
                list.add(
                    QuizQuestion(
                        "What was in the flower pot?",
                        listOf("A coin", "A spider", "A rusty key", "A ring"),
                        2
                    )
                )
                list.add(
                    QuizQuestion(
                        "Which room was locked?",
                        listOf("Basement", "Library", "Attic", "Kitchen"),
                        2
                    )
                )
                list.add(
                    QuizQuestion(
                        "How did he feel at first?",
                        listOf("Excited", "Tired & disappointed", "Happy", "Angry"),
                        1
                    )
                )
            }

            // GRAMMAR
            "Past Continuous" -> {
                list.add(
                    QuizQuestion(
                        "Choose correct: 'I ____ sleeping.'",
                        listOf("was", "were", "are", "is"),
                        0
                    )
                )
                list.add(
                    QuizQuestion(
                        "They _____ soccer at 5 PM.",
                        listOf("was playing", "were playing", "played", "plays"),
                        1
                    )
                )
                list.add(
                    QuizQuestion(
                        "_____ she working all day?",
                        listOf("Did", "Was", "Were", "Is"),
                        1
                    )
                )
                list.add(
                    QuizQuestion(
                        "What were you _____?",
                        listOf("do", "did", "doing", "done"),
                        2
                    )
                )
                list.add(
                    QuizQuestion(
                        "We were _____ TV.",
                        listOf("watch", "watched", "watching", "watches"),
                        2
                    )
                )
            }

            // VOCABULARY
            "Airport" -> {
                list.add(
                    QuizQuestion(
                        "Document to board the plane?",
                        listOf("Ticket", "Boarding Pass", "License", "Map"),
                        1
                    )
                )
                list.add(
                    QuizQuestion(
                        "Where do you get bags?",
                        listOf("Check-in", "Security", "Baggage Claim", "Gate"),
                        2
                    )
                )
                list.add(
                    QuizQuestion(
                        "Exit to the plane?",
                        listOf("Door", "Gate", "Hall", "Window"),
                        1
                    )
                )
                list.add(
                    QuizQuestion(
                        "You show passport at...",
                        listOf("Immigration", "Restaurant", "Taxi", "Toilet"),
                        0
                    )
                )
                list.add(
                    QuizQuestion(
                        "Pilot flies the...",
                        listOf("Bus", "Train", "Plane", "Car"),
                        2
                    )
                )
            }

            // Fallback Genérico
            else -> {
                list.add(
                    QuizQuestion(
                        "Is this a placeholder?",
                        listOf("Yes", "No", "Maybe", "Idk"),
                        0
                    )
                )
                list.add(QuizQuestion("Is the sky blue?", listOf("No", "Yes", "Maybe", "Green"), 1))
                list.add(QuizQuestion("2 + 2 = ?", listOf("3", "5", "4", "22"), 2))
                list.add(QuizQuestion("Cats say...", listOf("Woof", "Moo", "Meow", "Quack"), 2))
                list.add(
                    QuizQuestion(
                        "Android is...",
                        listOf("An Apple", "An OS", "A car", "A house"),
                        1
                    )
                )
            }
        }
        return list
    }

    override fun onDestroy() {
        SoundManager.release()
        super.onDestroy()
    }
}
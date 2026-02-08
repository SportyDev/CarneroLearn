package com.sportydev.carnerolearnrenewed.data.model

data class QuizQuestion(
    val question: String,
    val options: List<String>, // Una lista de 3 o 4 opciones
    val correctAnswerIndex: Int // El índice de la respuesta correcta (0, 1, 2, 3)
)
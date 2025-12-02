package com.sportydev.carnerolearnrenewed

import java.io.Serializable

data class Word(
    val english: String,
    val spanish: String,
    val phonetic: String,
    val type: String, // ej: "noun", "verb", "adj"
    val definition: String,
    val example: String,
    var isMastered: Boolean = false, // Para el Checkbox
    var isFavorite: Boolean = false
) : Serializable
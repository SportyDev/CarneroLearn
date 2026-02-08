package com.sportydev.carnerolearnrenewed.data.model

// --- TABLA: User ---
data class User(
    val id: Int,
    val name: String,
    val email: String?,
    val currentLevel: String?, // DEFAULT 'Beginner'
    val xp: Int,              // DEFAULT 0
    val streakDays: Int,      // DEFAULT 0
    val lastLoginDate: String?,
    val lessonsCompleted: Int // DEFAULT 0
)

// --- TABLA: Achievements ---
data class Achievement(
    val id: Int,
    val name: String,
    val description: String?,
    val conditionType: String?,
    val conditionValue: Int?
)

// --- TABLA: Grammar_Topics ---
data class GrammarTopic(
    val id: Int,
    val title: String,
    val level: String?,
    val structureFormula: String?,
    val explanation: String?,
    val examples: String?
)

// --- TABLA: Vocab_Categories ---
data class VocabCategory(
    val id: Int,
    val name: String
)

// --- TABLA: Words  ---
data class Word(
    val id: Int,
    val categoryId: Int,       // En BD es category_id
    val word: String,
    val translation: String?,
    val phonetic: String?,
    val type: String?,
    val exampleSentence: String? // En BD es example_sentence
)

// --- TABLA: Listening_Resources ---
data class ListeningResource(
    val id: Int,
    val title: String?,
    val contentText: String?,
    val level: String?,
    val durationApprox: Int?,
    val speakerGender: String?
)

// --- TABLA: Reading_Resources ---
data class ReadingResource(
    val id: Int,
    val title: String?,
    val synopsis: String?,
    val contentText: String?,
    val level: String?,
    val durationRead: Int?
)

// --- TABLA: Questions ---
data class Question(
    val id: Int,
    val contextType: String?,
    val contextId: Int?,
    val questionText: String,
    val explanation: String?,
    val difficulty: Int
)

// --- TABLA: Question_Options ---
data class QuestionOption(
    val id: Int,
    val questionId: Int,
    val optionText: String,
    val isCorrect: Boolean // SQLite guarda 0 o 1, lo convertiremos en el AdminBd
)
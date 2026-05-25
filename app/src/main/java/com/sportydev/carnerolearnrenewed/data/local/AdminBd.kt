package com.sportydev.carnerolearnrenewed.data.local

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sportydev.carnerolearnrenewed.data.model.*
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AdminBd(private val contexto: Context) :
    SQLiteOpenHelper(contexto, DATABASE_NAME, null, DATABASE_VERSION) {

    private val dbPath: String = contexto.getDatabasePath(DATABASE_NAME).path

    companion object {
        private const val DATABASE_NAME = "ingles.db"
        private const val DATABASE_VERSION = 1
    }

    init {
        createDatabase()
    }

    fun createDatabase() {
        if (!databaseExists()) {
            this.readableDatabase
            this.close()
            try {
                copyDatabase()
            } catch (e: IOException) {
                throw Error("Error copiando la base de datos")
            }
        }
    }

    private fun databaseExists(): Boolean {
        return contexto.getDatabasePath(DATABASE_NAME).exists()
    }

    private fun copyDatabase() {
        val inputStream = contexto.assets.open(DATABASE_NAME)
        val outputStream = FileOutputStream(dbPath)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }

    override fun onCreate(db: SQLiteDatabase?) {}
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    // ------------------------------------------------------------------
    // MÉTODOS DE USUARIO
    // ------------------------------------------------------------------

    @SuppressLint("Range")
    fun getUserData(): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM User LIMIT 1", null)
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndex("id")),
                name = cursor.getString(cursor.getColumnIndex("name")),
                email = cursor.getString(cursor.getColumnIndex("email")),
                currentLevel = cursor.getString(cursor.getColumnIndex("current_level")),
                xp = cursor.getInt(cursor.getColumnIndex("xp")),
                streakDays = cursor.getInt(cursor.getColumnIndex("streak_days")),
                lastLoginDate = cursor.getString(cursor.getColumnIndex("last_login_date")),
                lessonsCompleted = cursor.getInt(cursor.getColumnIndex("lessons_completed"))
            )
        }
        cursor.close()
        return user
    }

    fun updateStreak() {
        val db = this.writableDatabase

        // Obtenemos el usuario actual (asumiendo ID 1)
        val cursor = db.rawQuery("SELECT * FROM User LIMIT 1", null)
        if (cursor.moveToFirst()) {

            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val currentStreak = cursor.getInt(cursor.getColumnIndexOrThrow("streak_days"))
            val lastLoginDate = cursor.getString(cursor.getColumnIndexOrThrow("last_login_date"))

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayStr = sdf.format(Date())

            if (lastLoginDate == todayStr) {
                // Ya inició sesión hoy, no hacemos nada
                cursor.close()
                return
            }

            var newStreak = 1 // Por defecto, si se rompió la racha o es la primera vez

            if (lastLoginDate != null) {
                val lastDate = sdf.parse(lastLoginDate)
                val todayDate = sdf.parse(todayStr)

                if (lastDate != null && todayDate != null) {
                    // Calcular diferencia en días
                    val diff = todayDate.time - lastDate.time
                    val daysDiff = (diff / (1000 * 60 * 60 * 24)).toInt()

                    if (daysDiff == 1) {
                        // Entró al día siguiente consecutivo
                        newStreak = currentStreak + 1
                    }
                }
            }

            // Actualizamos la base de datos
            val values = ContentValues().apply {
                put("streak_days", newStreak)
                put("last_login_date", todayStr)
            }
            db.update("User", values, "id = ?", arrayOf(id.toString()))
        }
        cursor.close()
    }

    // ------------------------------------------------------------------
    // MÉTODOS PARA CONSULTAR DATOS
    // ------------------------------------------------------------------

    @SuppressLint("Range")
    fun getAllGrammarTopics(): List<GrammarTopic> {
        val topicList = mutableListOf<GrammarTopic>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Grammar_Topics", null)
        if (cursor.moveToFirst()) {
            do {
                topicList.add(
                    GrammarTopic(
                        id = cursor.getInt(cursor.getColumnIndex("id")),
                        title = cursor.getString(cursor.getColumnIndex("title")),
                        level = cursor.getString(cursor.getColumnIndex("level")),
                        structureFormula = cursor.getString(cursor.getColumnIndex("structure_formula")),
                        explanation = cursor.getString(cursor.getColumnIndex("explanation")),
                        examples = cursor.getString(cursor.getColumnIndex("examples"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return topicList
    }

    @SuppressLint("Range")
    fun getAllVocabCategories(): List<VocabCategory> {
        val categories = mutableListOf<VocabCategory>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Vocab_Categories", null)
        if (cursor.moveToFirst()) {
            do {
                categories.add(
                    VocabCategory(
                        id = cursor.getInt(cursor.getColumnIndex("id")),
                        name = cursor.getString(cursor.getColumnIndex("name"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return categories
    }

    @SuppressLint("Range")
    fun getAllWords(): List<Word> {
        val wordList = mutableListOf<Word>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Words", null)
        if (cursor.moveToFirst()) {
            do {
                wordList.add(
                    Word(
                        id = cursor.getInt(cursor.getColumnIndex("id")),
                        categoryId = cursor.getInt(cursor.getColumnIndex("category_id")),
                        word = cursor.getString(cursor.getColumnIndex("word")),
                        translation = cursor.getString(cursor.getColumnIndex("translation")),
                        phonetic = cursor.getString(cursor.getColumnIndex("phonetic")),
                        type = cursor.getString(cursor.getColumnIndex("type")),
                        exampleSentence = cursor.getString(cursor.getColumnIndex("example_sentence"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return wordList
    }

    @SuppressLint("Range")
    fun getWordsByCategoryId(categoryId: Int): List<Word> {
        val wordList = mutableListOf<Word>()
        val db = this.readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM Words WHERE category_id = ?", arrayOf(categoryId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val word = Word(
                    id = cursor.getInt(0),
                    categoryId = cursor.getInt(1),
                    word = cursor.getString(2),
                    translation = cursor.getString(3),
                    phonetic = cursor.getString(4),
                    type = cursor.getString(5),
                    exampleSentence = cursor.getString(6)
                )
                wordList.add(word)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return wordList
    }

    @SuppressLint("Range")
    fun getWordsByCategoryName(categoryName: String): List<Word> {
        val wordList = mutableListOf<Word>()
        val db = this.readableDatabase

        val query = """
            SELECT w.* FROM Words w 
            INNER JOIN Vocab_Categories c ON w.category_id = c.id 
            WHERE c.name = ?
        """

        val cursor = db.rawQuery(query, arrayOf(categoryName))

        if (cursor.moveToFirst()) {
            do {
                val wordObj = Word(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    categoryId = cursor.getInt(cursor.getColumnIndex("category_id")),
                    word = cursor.getString(cursor.getColumnIndex("word")),
                    translation = cursor.getString(cursor.getColumnIndex("translation")),
                    phonetic = cursor.getString(cursor.getColumnIndex("phonetic")),
                    type = cursor.getString(cursor.getColumnIndex("type")),
                    exampleSentence = cursor.getString(cursor.getColumnIndex("example_sentence"))
                )
                wordList.add(wordObj)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return wordList
    }

    @SuppressLint("Range")
    fun getAllReadingResources(): List<ReadingResource> {
        val readingList = mutableListOf<ReadingResource>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Reading_Resources", null)
        if (cursor.moveToFirst()) {
            do {
                readingList.add(
                    ReadingResource(
                        id = cursor.getInt(cursor.getColumnIndex("id")),
                        title = cursor.getString(cursor.getColumnIndex("title")),
                        synopsis = cursor.getString(cursor.getColumnIndex("synopsis")),
                        contentText = cursor.getString(cursor.getColumnIndex("content_text")),
                        level = cursor.getString(cursor.getColumnIndex("level")),
                        durationRead = cursor.getInt(cursor.getColumnIndex("duration_read"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return readingList
    }

    @SuppressLint("Range")
    fun getReadingResourceById(id: Int): ReadingResource? {
        val db = this.readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM Reading_Resources WHERE id = ?", arrayOf(id.toString()))
        var resource: ReadingResource? = null
        if (cursor.moveToFirst()) {
            resource = ReadingResource(
                id = cursor.getInt(cursor.getColumnIndex("id")),
                title = cursor.getString(cursor.getColumnIndex("title")),
                synopsis = cursor.getString(cursor.getColumnIndex("synopsis")),
                contentText = cursor.getString(cursor.getColumnIndex("content_text")),
                level = cursor.getString(cursor.getColumnIndex("level")),
                durationRead = cursor.getInt(cursor.getColumnIndex("duration_read"))
            )
        }
        cursor.close()
        return resource
    }

    @SuppressLint("Range")
    fun getRandomVerb(): IrregularVerb? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Irregular_Verbs ORDER BY RANDOM() LIMIT 1", null)
        var verb: IrregularVerb? = null
        if (cursor.moveToFirst()) {
            verb = IrregularVerb(
                id = cursor.getInt(cursor.getColumnIndex("id")),
                baseForm = cursor.getString(cursor.getColumnIndex("base_form")),
                pastSimple = cursor.getString(cursor.getColumnIndex("past_simple")),
                pastParticiple = cursor.getString(cursor.getColumnIndex("past_participle")),
                translation = cursor.getString(cursor.getColumnIndex("translation")),
                patternGroup = cursor.getString(cursor.getColumnIndex("pattern_group"))
            )
        }
        cursor.close()
        return verb
    }

    @SuppressLint("Range")
    fun getRandomDistractors(excludeId: Int, limit: Int): List<IrregularVerb> {
        val distractors = mutableListOf<IrregularVerb>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM Irregular_Verbs WHERE id != ? ORDER BY RANDOM() LIMIT ?",
            arrayOf(excludeId.toString(), limit.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                distractors.add(
                    IrregularVerb(
                        id = cursor.getInt(cursor.getColumnIndex("id")),
                        baseForm = cursor.getString(cursor.getColumnIndex("base_form")),
                        pastSimple = cursor.getString(cursor.getColumnIndex("past_simple")),
                        pastParticiple = cursor.getString(cursor.getColumnIndex("past_participle")),
                        translation = cursor.getString(cursor.getColumnIndex("translation")),
                        patternGroup = cursor.getString(cursor.getColumnIndex("pattern_group"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return distractors
    }

    // 1. Función para guardar el resultado al terminar el minijuego
    fun insertQuizResult(score: Int, total: Int) {
        val db = this.writableDatabase
        // Obtenemos la fecha actual en formato AAAA-MM-DD
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateStr = dateFormat.format(Date())

        val query =
            "INSERT INTO Quiz_History (date_played, score, total) VALUES ('$dateStr', $score, $total)"
        db.execSQL(query)
    }

    // 2. Función para obtener los últimos 5 juegos (Para la gráfica)
    @SuppressLint("Range")
    fun getLastFiveQuizResults(): List<QuizResult> {
        val list = mutableListOf<QuizResult>()
        val db = this.readableDatabase
        // Ordenamos por ID descendente para tener los más nuevos, y limitamos a 5
        val cursor = db.rawQuery("SELECT * FROM Quiz_History ORDER BY id DESC LIMIT 5", null)

        if (cursor.moveToFirst()) {
            do {
                val result = QuizResult(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    datePlayed = cursor.getString(cursor.getColumnIndex("date_played")),
                    score = cursor.getInt(cursor.getColumnIndex("score")),
                    total = cursor.getInt(cursor.getColumnIndex("total"))
                )
                list.add(result)
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Volteamos la lista para que el más viejo de los 5 quede primero (ideal para pintar gráficas de izquierda a derecha)
        return list.reversed()
    }

    // Obtener la suma total de respuestas correctas (Mastered)
    fun getTotalMasteredVerbs(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(score) FROM Quiz_History", null)
        var total = 0
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0)
        }
        cursor.close()
        return total
    }

    // Obtener todas las fechas únicas jugadas para calcular la racha
    @SuppressLint("Range")
    fun getPlayedDates(): List<String> {
        val list = mutableListOf<String>()
        val db = this.readableDatabase
        // Obtenemos las fechas únicas ordenadas de la más reciente a la más antigua
        val cursor = db.rawQuery(
            "SELECT DISTINCT date_played FROM Quiz_History ORDER BY date_played DESC",
            null
        )
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    @SuppressLint("Range")
    fun getAllIrregularVerbs(): List<IrregularVerb> {
        val list = mutableListOf<IrregularVerb>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Irregular_Verbs ORDER BY base_form ASC", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    IrregularVerb(
                        id = cursor.getInt(cursor.getColumnIndex("id")),
                        baseForm = cursor.getString(cursor.getColumnIndex("base_form")),
                        pastSimple = cursor.getString(cursor.getColumnIndex("past_simple")),
                        pastParticiple = cursor.getString(cursor.getColumnIndex("past_participle")),
                        translation = cursor.getString(cursor.getColumnIndex("translation")),
                        patternGroup = cursor.getString(cursor.getColumnIndex("pattern_group"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    @SuppressLint("Range")
    fun getRandomVerbsByPattern(pattern: String, limit: Int): List<IrregularVerb> {
        val list = mutableListOf<IrregularVerb>()
        val db = this.readableDatabase
        // Buscamos verbos que tengan el MISMO patrón, de forma aleatoria
        val cursor = db.rawQuery(
            "SELECT * FROM Irregular_Verbs WHERE pattern_group = ? ORDER BY RANDOM() LIMIT ?",
            arrayOf(pattern, limit.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    IrregularVerb(
                        id = cursor.getInt(cursor.getColumnIndex("id")),
                        baseForm = cursor.getString(cursor.getColumnIndex("base_form")),
                        pastSimple = cursor.getString(cursor.getColumnIndex("past_simple")),
                        pastParticiple = cursor.getString(cursor.getColumnIndex("past_participle")),
                        translation = cursor.getString(cursor.getColumnIndex("translation")),
                        patternGroup = cursor.getString(cursor.getColumnIndex("pattern_group"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    @SuppressLint("Range")
    fun getLastFiveDaysResults(): List<QuizResult> {
        val list = mutableListOf<QuizResult>()
        val db = this.readableDatabase

        // Magia SQL: Agrupamos por día y sumamos el puntaje y el total
        val query = """
            SELECT date_played, SUM(score) as daily_score, SUM(total) as daily_total 
            FROM Quiz_History 
            GROUP BY date_played 
            ORDER BY date_played DESC 
            LIMIT 5
        """.trimIndent()

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val result = QuizResult(
                    id = 0, // El ID ya no importa porque es un resumen del día
                    datePlayed = cursor.getString(cursor.getColumnIndex("date_played")),
                    score = cursor.getInt(cursor.getColumnIndex("daily_score")),
                    total = cursor.getInt(cursor.getColumnIndex("daily_total"))
                )
                list.add(result)
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Volteamos la lista para que el día más antiguo quede a la izquierda y "Hoy" a la derecha
        return list.reversed()
    }

    @SuppressLint("Range")
    fun getRandomQuizQuestions(limit: Int = 10): List<QuizQuestion> {
        val questions = mutableListOf<QuizQuestion>()
        val db = this.readableDatabase

        // 1. Obtener preguntas aleatorias
        val queryQuestions = "SELECT * FROM Questions ORDER BY RANDOM() LIMIT ?"
        val cursor = db.rawQuery(queryQuestions, arrayOf(limit.toString()))

        if (cursor.moveToFirst()) {
            do {
                val questionId = cursor.getInt(cursor.getColumnIndex("id"))
                val questionText = cursor.getString(cursor.getColumnIndex("question_text"))

                // 2. Obtener las opciones para esta pregunta específica
                val options = mutableListOf<String>()
                var correctIndex = 0

                val queryOptions = "SELECT * FROM Question_Options WHERE question_id = ?"
                val optCursor = db.rawQuery(queryOptions, arrayOf(questionId.toString()))

                if (optCursor.moveToFirst()) {
                    var index = 0
                    do {
                        val optionText =
                            optCursor.getString(optCursor.getColumnIndex("option_text"))
                        val isCorrect =
                            optCursor.getInt(optCursor.getColumnIndex("is_correct")) == 1

                        options.add(optionText)
                        if (isCorrect) correctIndex = index
                        index++
                    } while (optCursor.moveToNext())
                }
                optCursor.close()

                // 3. Agregar a la lista (asumiendo que QuizQuestion acepta estos parámetros)
                questions.add(QuizQuestion(questionText, options, correctIndex))

            } while (cursor.moveToNext())
        }
        cursor.close()
        return questions
    }

    // 1. Obtener preguntas (Ahora es INMUNE a mayúsculas/minúsculas)
    @SuppressLint("Range")
    fun getQuizQuestionsByContext(
        contextType: String,
        contextId: Int,
        limit: Int = 10
    ): List<QuizQuestion> {
        val questions = mutableListOf<QuizQuestion>()
        val db = this.readableDatabase

        // MAGIA AQUÍ: UPPER(context_type) = UPPER(?)
        val queryQuestions = """
            SELECT * FROM Questions 
            WHERE UPPER(context_type) = UPPER(?) AND context_id = ? 
            ORDER BY RANDOM() LIMIT ?
        """.trimIndent()

        val cursor = db.rawQuery(
            queryQuestions,
            arrayOf(contextType, contextId.toString(), limit.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                val questionId = cursor.getInt(cursor.getColumnIndex("id"))
                val questionText = cursor.getString(cursor.getColumnIndex("question_text"))

                val options = mutableListOf<String>()
                var correctIndex = 0

                val queryOptions = "SELECT * FROM Question_Options WHERE question_id = ?"
                val optCursor = db.rawQuery(queryOptions, arrayOf(questionId.toString()))

                if (optCursor.moveToFirst()) {
                    var index = 0
                    do {
                        options.add(optCursor.getString(optCursor.getColumnIndex("option_text")))
                        if (optCursor.getInt(optCursor.getColumnIndex("is_correct")) == 1) {
                            correctIndex = index
                        }
                        index++
                    } while (optCursor.moveToNext())
                }
                optCursor.close()

                questions.add(QuizQuestion(questionText, options, correctIndex))

            } while (cursor.moveToNext())
        }
        cursor.close()
        return questions
    }

    // 2. Contar preguntas (Ahora es INMUNE a mayúsculas/minúsculas)
    fun getQuestionCountForContext(contextType: String, contextId: Int): Int {
        val db = this.readableDatabase
        // MAGIA AQUÍ: UPPER(context_type) = UPPER(?)
        val query =
            "SELECT COUNT(*) FROM Questions WHERE UPPER(context_type) = UPPER(?) AND context_id = ?"
        val cursor = db.rawQuery(query, arrayOf(contextType, contextId.toString()))
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    // --- GENERADOR INFINITO DE VOCABULARIO ---
    @SuppressLint("Range")
    fun generateVocabularyQuiz(categoryId: Int, limit: Int = 10): List<QuizQuestion> {
        val quizQuestions = mutableListOf<QuizQuestion>()
        val db = this.readableDatabase

        // 1. Elegimos palabras aleatorias de esta categoría específica
        val queryWords = "SELECT * FROM Words WHERE category_id = ? ORDER BY RANDOM() LIMIT ?"
        val cursor = db.rawQuery(queryWords, arrayOf(categoryId.toString(), limit.toString()))

        if (cursor.moveToFirst()) {
            do {
                val englishWord = cursor.getString(cursor.getColumnIndex("word"))
                val correctTranslation = cursor.getString(cursor.getColumnIndex("translation"))

                // 2. Buscamos 3 "distractores" (traducciones de otras palabras) para engañar al usuario
                val distractors = mutableListOf<String>()
                val queryDistractors = """
                    SELECT translation FROM Words 
                    WHERE translation != ? AND translation IS NOT NULL 
                    ORDER BY RANDOM() LIMIT 3
                """.trimIndent()

                val distCursor = db.rawQuery(queryDistractors, arrayOf(correctTranslation))

                if (distCursor.moveToFirst()) {
                    do {
                        distractors.add(distCursor.getString(distCursor.getColumnIndex("translation")))
                    } while (distCursor.moveToNext())
                }
                distCursor.close()

                // 3. Juntamos la respuesta correcta con las falsas y las revolvemos
                val allOptions = mutableListOf<String>()
                allOptions.add(correctTranslation) // La correcta
                allOptions.addAll(distractors)     // Las 3 falsas
                allOptions.shuffle()               // ¡Las mezclamos!

                // 4. Encontramos en qué botón quedó la correcta después de mezclar
                val correctIndex = allOptions.indexOf(correctTranslation)

                // 5. Armamos la pregunta final
                val questionText = "What does the word mean'$englishWord'?"

                quizQuestions.add(QuizQuestion(questionText, allOptions, correctIndex))

            } while (cursor.moveToNext())
        }
        cursor.close()
        return quizQuestions
    }
}
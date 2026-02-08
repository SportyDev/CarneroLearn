package com.sportydev.carnerolearnrenewed.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sportydev.carnerolearnrenewed.data.model.GrammarTopic
import com.sportydev.carnerolearnrenewed.data.model.Word
import java.io.FileOutputStream
import java.io.IOException

class AdminBd(private val contexto: Context) : SQLiteOpenHelper(contexto, DATABASE_NAME, null, DATABASE_VERSION) {

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
    // MÉTODOS PARA CONSULTAR DATOS
    // ------------------------------------------------------------------

    // OBTENER TODAS LAS PALABRAS
    @SuppressLint("Range")
    fun getAllWords(): List<Word> {
        val wordList = mutableListOf<Word>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Words", null)

        if (cursor.moveToFirst()) {
            do {
                val wordObj = Word(
                    // Izquierda: Variable en Kotlin (Models.kt)
                    // Derecha: Columna exacta en tu SQLite (snake_case)
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

    // OBTENER TEMAS DE GRAMÁTICA
    @SuppressLint("Range")
    fun getAllGrammarTopics(): List<GrammarTopic> {
        val topicList = mutableListOf<GrammarTopic>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Grammar_Topics", null)

        if (cursor.moveToFirst()) {
            do {
                val topic = GrammarTopic(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    title = cursor.getString(cursor.getColumnIndex("title")),
                    level = cursor.getString(cursor.getColumnIndex("level")),
                    structureFormula = cursor.getString(cursor.getColumnIndex("structure_formula")),
                    explanation = cursor.getString(cursor.getColumnIndex("explanation")),
                    examples = cursor.getString(cursor.getColumnIndex("examples"))
                )
                topicList.add(topic)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return topicList
    }

    // OBTENER PALABRAS POR CATEGORIA
    @SuppressLint("Range")
    fun getWordsByCategoryName(categoryName: String): List<Word> {
        val wordList = mutableListOf<Word>()
        val db = this.readableDatabase

        // Hacemos un JOIN para unir la tabla Words con Vocab_Categories
        // Buscamos las palabras donde el nombre de la categoría coincida
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
}
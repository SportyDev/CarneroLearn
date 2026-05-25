package com.sportydev.carnerolearnrenewed.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.model.Word
import com.sportydev.carnerolearnrenewed.utils.TtsManager
import java.util.Locale

class WordAdapter(
    private var wordList: List<Word>,
    private val categoryColorHex: String
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    private var filteredList: List<Word> = wordList

    class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvWord: TextView = view.findViewById(R.id.tvWord)
        val tvPhonetic: TextView = view.findViewById(R.id.tvPhonetic)
        val tvType: TextView = view.findViewById(R.id.tvPartOfSpeech)
        val tvDefinition: TextView = view.findViewById(R.id.tvDefinition)
        val tvExample: TextView = view.findViewById(R.id.tvExample)
        val tvTranslation: TextView = view.findViewById(R.id.tvTranslation)
        val btnAudio: ImageButton = view.findViewById(R.id.btnPronounce)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word_card, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = filteredList[position]
        
        holder.tvWord.text = word.word
        holder.tvPhonetic.text = word.phonetic ?: ""
        holder.tvType.text = word.type ?: ""
        holder.tvDefinition.text = word.translation // En item_word_card.xml tvDefinition parece ser el significado principal
        holder.tvExample.text = word.exampleSentence ?: ""
        holder.tvTranslation.text = word.translation
        
        // Aplicar color de la categoría a la traducción
        try {
            holder.tvTranslation.setTextColor(Color.parseColor(categoryColorHex))
        } catch (e: Exception) {
            holder.tvTranslation.setTextColor(Color.BLACK)
        }

        // Acción de Audio
        holder.btnAudio.setOnClickListener {
            TtsManager.speak(word.word)
        }
    }

    override fun getItemCount() = filteredList.size

    fun filter(query: String) {
        val lowerQuery = query.lowercase(Locale.ROOT)
        filteredList = if (lowerQuery.isEmpty()) {
            wordList
        } else {
            wordList.filter { 
                it.word.lowercase(Locale.ROOT).contains(lowerQuery) ||
                (it.translation?.lowercase(Locale.ROOT)?.contains(lowerQuery) == true)
            }
        }
        notifyDataSetChanged()
    }

    fun updateList(newList: List<Word>) {
        wordList = newList
        filteredList = newList
        notifyDataSetChanged()
    }
}
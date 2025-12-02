package com.sportydev.carnerolearnrenewed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordAdapter(private val words: List<Word>) :
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvWord: TextView = view.findViewById(R.id.tvWord)
        val tvPhonetic: TextView = view.findViewById(R.id.tvPhonetic)
        val tvType: TextView = view.findViewById(R.id.tvPartOfSpeech)
        val tvDefinition: TextView = view.findViewById(R.id.tvDefinition)
        val tvExample: TextView = view.findViewById(R.id.tvExample)
        val tvTranslation: TextView = view.findViewById(R.id.tvTranslation)
        val btnPronounce: ImageButton = view.findViewById(R.id.btnPronounce)
        val chkMastered: CheckBox = view.findViewById(R.id.chkMastered)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word_card, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]

        holder.tvWord.text = word.english
        holder.tvPhonetic.text = word.phonetic
        holder.tvType.text = word.type
        holder.tvDefinition.text = word.definition
        holder.tvExample.text = "\"${word.example}\""
        holder.tvTranslation.text = word.spanish
        holder.chkMastered.isChecked = word.isMastered

        // --- LÓGICA DE PRONUNCIACIÓN ---
        holder.btnPronounce.setOnClickListener {
            // Usamos nuestro Singleton para hablar
            TtsManager.speak(word.english)
        }

        // Lógica del Checkbox (Solo visual por ahora)
        holder.chkMastered.setOnCheckedChangeListener { _, isChecked ->
            word.isMastered = isChecked
        }
    }

    override fun getItemCount() = words.size
}
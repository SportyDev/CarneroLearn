package com.sportydev.carnerolearnrenewed.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.utils.TtsManager
import com.sportydev.carnerolearnrenewed.data.model.Word

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

        // 1. Asignamos el campo 'word' (antes english)
        holder.tvWord.text = word.word

        // 2. Manejamos nulos con el operador Elvis (?:) por si la BD viene vacía
        holder.tvPhonetic.text = word.phonetic ?: ""
        holder.tvType.text = word.type ?: ""

        // 3. LA BD NO TIENE DEFINICIÓN EN INGLÉS, SOLO TRADUCCIÓN.
        // Ocultamos el campo de definición para que no estorbe.
        holder.tvDefinition.visibility = View.GONE
        // O si prefieres usar la traducción ahí, descomenta la siguiente línea:
        // holder.tvDefinition.text = word.translation

        // 4. Asignamos el ejemplo (antes example)
        if (word.exampleSentence != null) {
            holder.tvExample.text = "\"${word.exampleSentence}\""
            holder.tvExample.visibility = View.VISIBLE
        } else {
            holder.tvExample.visibility = View.GONE
        }

        // 5. Asignamos la traducción (antes spanish)
        holder.tvTranslation.text = word.translation ?: "Sin traducción"

        // 6. Configurar TTS con la palabra en inglés
        holder.btnPronounce.setOnClickListener {
            TtsManager.speak(word.word)
        }

        // 7. LÓGICA DEL CHECKBOX (TEMPORALMENTE DESACTIVADA)
        // Como el modelo 'Word' viene directo de la BD y no tiene campo 'isMastered',
        // no podemos guardar el estado aquí directamente sin otra tabla.
        holder.chkMastered.isChecked = false
        holder.chkMastered.isEnabled = false // Lo deshabilitamos visualmente por ahora

        /* Lógica futura: Aquí deberías consultar a la tabla 'User_Favorite_Words'
           para ver si esta palabra está marcada.
        */
    }

    override fun getItemCount() = words.size
}
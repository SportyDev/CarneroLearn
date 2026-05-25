package com.sportydev.carnerolearnrenewed.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.model.GrammarTopic
import java.util.Locale
import kotlin.math.abs

class GrammarAdapter(
    private var fullList: List<GrammarTopic>,
    private val onTopicClick: (GrammarTopic, String, Int) -> Unit
) : RecyclerView.Adapter<GrammarAdapter.TopicViewHolder>() {

    private var filteredList: List<GrammarTopic> = fullList

    // 1. Definimos una paleta de colores vibrantes estilo Material Design
    private val materialColors = listOf(
        "#EF5350", // Rojo
        "#AB47BC", // Morado
        "#5C6BC0", // Indigo
        "#29B6F6", // Azul claro
        "#26A69A", // Verde azulado (Teal)
        "#66BB6A", // Verde
        "#FFA726", // Naranja
        "#FF7043"  // Naranja oscuro
    )

    class TopicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.cardTopic)
        val ivIcon: ImageView = view.findViewById(R.id.ivTopicIcon)
        val tvTitle: TextView = view.findViewById(R.id.tvTopicTitle)
        val tvLevel: TextView = view.findViewById(R.id.tvTopicLevel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grammar_topic, parent, false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = filteredList[position]
        holder.tvTitle.text = topic.title
        holder.tvLevel.text = "Grammar • ${topic.level ?: "All"}"

        // Calculamos el color y el icono
        val colorIndex = Math.abs(topic.title.hashCode()) % materialColors.size
        val colorHex = materialColors[colorIndex] // Guardamos el texto del color (ej. "#EF5350")
        val dynamicColor = Color.parseColor(colorHex)
        val iconRes = getIconResource(topic.title) // Guardamos el ID del icono

        holder.ivIcon.setImageResource(iconRes)
        holder.ivIcon.setBackgroundResource(R.drawable.circle_background)
        holder.ivIcon.imageTintList = ColorStateList.valueOf(dynamicColor)

        // 2. AQUÍ ESTÁ LA MAGIA: Al hacer clic, enviamos todo al Activity
        holder.card.setOnClickListener {
            onTopicClick(topic, colorHex, iconRes)
        }
    }

    override fun getItemCount() = filteredList.size

    // 5. Expandimos las palabras clave para abarcar más temas
    private fun getIconResource(title: String): Int {
        val lowerTitle = title.lowercase(Locale.ROOT)
        return when {
            lowerTitle.contains("past") || lowerTitle.contains("present") ||
                    lowerTitle.contains("future") || lowerTitle.contains("tense") -> R.drawable.ic_time

            lowerTitle.contains("conditional") || lowerTitle.contains("if") -> R.drawable.ic_call_split

            lowerTitle.contains("pronoun") || lowerTitle.contains("subject") -> R.drawable.ic_person // Asumiendo que agregas este icono

            lowerTitle.contains("noun") || lowerTitle.contains("article") -> R.drawable.ic_label

            lowerTitle.contains("verb") -> R.drawable.ic_bolt

            lowerTitle.contains("question") -> R.drawable.ic_education // Asumiendo que agregas este icono

            lowerTitle.contains("adjective") || lowerTitle.contains("adverb") -> R.drawable.ic_checkmark // Asumiendo que agregas este icono

            else -> R.drawable.ic_book
        }
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter {
                it.title.contains(query, ignoreCase = true) ||
                        (it.level?.contains(query, ignoreCase = true) == true)
            }
        }
        notifyDataSetChanged()
    }

    fun updateList(newList: List<GrammarTopic>) {
        fullList = newList
        filteredList = newList
        notifyDataSetChanged()
    }
}
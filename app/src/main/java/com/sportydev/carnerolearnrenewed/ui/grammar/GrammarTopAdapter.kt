package com.sportydev.carnerolearnrenewed.ui.grammar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.model.GrammarTopic

class GrammarTopAdapter(
    //Lista de temas que se mostraran
    private var topicList: List<GrammarTopic>,
    // Función que se ejecutará cuando el usuario toque un tema
    private val onClick: (GrammarTopic) -> Unit
) : RecyclerView.Adapter<GrammarTopAdapter.TopicViewHolder>() {

    class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //Conectamos el xml
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvLevel: TextView = itemView.findViewById(R.id.tvLevel)
        val tvExplanation: TextView = itemView.findViewById(R.id.tvExplanation)
    }

    //Este metodo crea una nueva "tarjeta visual" cuando el RecyclerView la necesita.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grammar_topics, parent, false)
        return TopicViewHolder(view)
    }

    // Este metodo conecta los datos con la tarjeta.
    //
    //        Aquí tomamos un tema de la lista
    //        y lo colocamos en los TextViews.
    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        //Obtencion d el  tema segun la posicion en la lista
        val topic = topicList[position]

        holder.tvTitle.text = topic.title
        holder.tvLevel.text = topic.level ?: ""
        holder.tvExplanation.text = topic.explanation ?: ""

        //El usuario hace click en la tajeta
        holder.itemView.setOnClickListener {
            onClick(topic)
        }
    }


   //Devuelve cuántos datos hay en la lista
    override fun getItemCount(): Int = topicList.size

    fun updateList(newList: List<GrammarTopic>) {
        topicList = newList
        notifyDataSetChanged()
    }
}
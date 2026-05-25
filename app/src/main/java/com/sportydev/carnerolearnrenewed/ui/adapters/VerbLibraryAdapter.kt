package com.sportydev.carnerolearnrenewed.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.model.IrregularVerb
import com.sportydev.carnerolearnrenewed.utils.TtsManager

// import com.sportydev.carnerolearnrenewed.utils.TtsManager

class VerbLibraryAdapter(
    private val verbs: List<IrregularVerb>
) : RecyclerView.Adapter<VerbLibraryAdapter.VerbViewHolder>() {

    class VerbViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBaseForm: TextView = view.findViewById(R.id.tvBaseForm)
        val tvForms: TextView = view.findViewById(R.id.tvForms)
        val tvTranslation: TextView = view.findViewById(R.id.tvTranslation)
        val tvPattern: TextView = view.findViewById(R.id.tvPattern)
        val btnSpeak: ImageButton = view.findViewById(R.id.btnSpeak)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerbViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_verb, parent, false)
        return VerbViewHolder(view)
    }

    override fun onBindViewHolder(holder: VerbViewHolder, position: Int) {
        val verb = verbs[position]
        holder.tvBaseForm.text = verb.baseForm
        holder.tvForms.text = "${verb.pastSimple} • ${verb.pastParticiple}"
        holder.tvTranslation.text = verb.translation
        holder.tvPattern.text = verb.patternGroup

        holder.btnSpeak.setOnClickListener {
            // El bot le lee las 3 formas para que el oído se acostumbre al patrón
            val textToSpeak = "${verb.baseForm}, ${verb.pastSimple}, ${verb.pastParticiple}"
            TtsManager.speak(textToSpeak)
        }
    }

    override fun getItemCount() = verbs.size
}
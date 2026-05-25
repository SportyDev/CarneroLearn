package com.sportydev.carnerolearnrenewed.ui.adapters

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.model.ReadingResource
import com.sportydev.carnerolearnrenewed.ui.reading.ReadingDetailActivity
import java.util.Locale

class ReadingAdapter(
    private var storyList: List<ReadingResource>
) : RecyclerView.Adapter<ReadingAdapter.StoryViewHolder>() {

    private var filteredList: List<ReadingResource> = storyList

    class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.cardStory)
        val tvLevel: TextView = view.findViewById(R.id.tvStoryLevel)
        val tvDuration: TextView = view.findViewById(R.id.tvStoryDuration)
        val tvTitle: TextView = view.findViewById(R.id.tvStoryTitle)
        val tvSynopsis: TextView = view.findViewById(R.id.tvStorySynopsis)
        val btnRead: MaterialButton = view.findViewById(R.id.btnReadNow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = filteredList[position]
        
        holder.tvTitle.text = story.title
        holder.tvSynopsis.text = story.synopsis
        holder.tvDuration.text = "${story.durationRead ?: 0} min read"
        holder.tvLevel.text = story.level

        // Aplicar colores según el nivel
        val (textColor, bgColor, btnColor) = when (story.level?.lowercase()) {
            "beginner" -> Triple("#4CAF50", "#E8F5E9", "#66BB6A")
            "intermediate" -> Triple("#FF9800", "#FFF3E0", "#FFB74D")
            "advanced" -> Triple("#F44336", "#FFEBEE", "#E57373")
            else -> Triple("#607D8B", "#ECEFF1", "#78909C")
        }

        holder.tvLevel.setTextColor(Color.parseColor(textColor))
        holder.tvLevel.setBackgroundColor(Color.parseColor(bgColor))
        holder.btnRead.setBackgroundColor(Color.parseColor(btnColor))

        val clickListener = View.OnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ReadingDetailActivity::class.java).apply {
                putExtra("EXTRA_STORY_ID", story.id)
            }
            context.startActivity(intent)
        }

        holder.card.setOnClickListener(clickListener)
        holder.btnRead.setOnClickListener(clickListener)
    }

    override fun getItemCount() = filteredList.size

    fun filter(query: String) {
        val lowerQuery = query.lowercase(Locale.ROOT)
        filteredList = if (lowerQuery.isEmpty()) {
            storyList
        } else {
            storyList.filter { 
                it.title?.lowercase(Locale.ROOT)?.contains(lowerQuery) == true ||
                it.synopsis?.lowercase(Locale.ROOT)?.contains(lowerQuery) == true
            }
        }
        notifyDataSetChanged()
    }

    fun updateList(newList: List<ReadingResource>) {
        storyList = newList
        filteredList = newList
        notifyDataSetChanged()
    }
}
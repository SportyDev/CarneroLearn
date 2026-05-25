package com.sportydev.carnerolearnrenewed.ui.adapters

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.data.model.VocabCategory
import com.sportydev.carnerolearnrenewed.ui.vocabulary.CategoryUIHelper
import com.sportydev.carnerolearnrenewed.ui.vocabulary.VocabularyDetailActivity
import java.util.Locale

class CategoryAdapter(
    private var fullList: List<VocabCategory>,
    private val onCategoryClick: (VocabCategory) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var filteredList: List<VocabCategory> = fullList

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.cardCategory)
        val ivIcon: ImageView = view.findViewById(R.id.ivCategoryIcon)
        val tvName: TextView = view.findViewById(R.id.tvCategoryName)
        val tvCount: TextView = view.findViewById(R.id.tvWordCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = filteredList[position]
        val uiParams = CategoryUIHelper.getUIForCategory(category.name)

        holder.tvName.text = category.name
        
        // Aplicar Colores dinámicos
        val mainColor = Color.parseColor(uiParams.mainColorHex)
        val lightColor = Color.parseColor(uiParams.lightColorHex)

        holder.tvName.setTextColor(mainColor)
        holder.ivIcon.setImageResource(uiParams.iconResId)
        holder.ivIcon.setColorFilter(mainColor)

        // Crear fondo circular por código
        val bgDrawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(lightColor)
        }
        holder.ivIcon.background = bgDrawable

        holder.card.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, VocabularyDetailActivity::class.java).apply {
                putExtra("EXTRA_CATEGORY_ID", category.id)
                putExtra("EXTRA_CATEGORY_NAME", category.name)
                putExtra("EXTRA_COLOR_HEX", uiParams.mainColorHex)
                putExtra("EXTRA_ICON_RES", uiParams.iconResId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = filteredList.size

    fun filter(query: String) {
        val lowerQuery = query.lowercase(Locale.ROOT)
        filteredList = if (lowerQuery.isEmpty()) {
            fullList
        } else {
            fullList.filter { it.name.lowercase(Locale.ROOT).contains(lowerQuery) }
        }
        notifyDataSetChanged()
    }

    fun updateList(newList: List<VocabCategory>) {
        fullList = newList
        filteredList = newList
        notifyDataSetChanged()
    }
}
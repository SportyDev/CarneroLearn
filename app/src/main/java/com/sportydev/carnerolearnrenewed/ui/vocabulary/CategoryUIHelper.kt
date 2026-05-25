package com.sportydev.carnerolearnrenewed.ui.vocabulary

import com.sportydev.carnerolearnrenewed.R
import java.util.Locale
import kotlin.math.abs

// Data class para empaquetar los 3 valores que necesita tu Adapter
data class CategoryUIParams(
    val mainColorHex: String,
    val lightColorHex: String,
    val iconResId: Int
)

object CategoryUIHelper {

    // Paleta de colores: [0] Color Principal (Fuerte), [1] Color de Fondo (Suave)
    private val colorPairs = listOf(
        Pair("#EF5350", "#FFEBEE"), // Rojo
        Pair("#AB47BC", "#F3E5F5"), // Morado
        Pair("#5C6BC0", "#E8EAF6"), // Indigo
        Pair("#29B6F6", "#E1F5FE"), // Azul claro
        Pair("#26A69A", "#E0F2F1"), // Verde azulado (Teal)
        Pair("#66BB6A", "#E8F5E9"), // Verde
        Pair("#FFA726", "#FFF3E0"), // Naranja
        Pair("#FF7043", "#FBE9E7"), // Naranja oscuro
        Pair("#EC407A", "#FCE4EC"), // Rosa
        Pair("#8D6E63", "#EFEBE9")  // Café
    )

    fun getUIForCategory(categoryName: String): CategoryUIParams {
        // Generamos un índice estable usando el nombre de la categoría
        val colorIndex = abs(categoryName.hashCode()) % colorPairs.size
        val colors = colorPairs[colorIndex]

        return CategoryUIParams(
            mainColorHex = colors.first,
            lightColorHex = colors.second,
            iconResId = getIconForCategory(categoryName)
        )
    }

    private fun getIconForCategory(name: String): Int {
        val lowerName = name.lowercase(Locale.ROOT)

        return when {
            // Comida / Cocina / Bebidas
            lowerName.contains("food") || lowerName.contains("cook") || lowerName.contains("kitchen") ||
                    lowerName.contains("drink") || lowerName.contains("dessert") || lowerName.contains("fruit") ||
                    lowerName.contains("vegetable") -> R.drawable.ic_restaurant

            // Familia / Relaciones / Personas
            lowerName.contains("family") || lowerName.contains("relation") || lowerName.contains("dating") ||
                    lowerName.contains("body") -> R.drawable.ic_person

            // Viajes / Vuelos
            lowerName.contains("travel") || lowerName.contains("airport") || lowerName.contains("flight") -> R.drawable.ic_airport

            // Transporte / Vehículos
            lowerName.contains("transport") || lowerName.contains("vehicle") || lowerName.contains("drive") -> R.drawable.ic_transportation

            // Trabajo / Negocios / Oficina
            lowerName.contains("work") || lowerName.contains("job") || lowerName.contains("profession") ||
                    lowerName.contains("business") || lowerName.contains("office") -> R.drawable.ic_business

            // Finanzas / Bancos
            lowerName.contains("bank") || lowerName.contains("finance") || lowerName.contains("economy") -> R.drawable.ic_money

            // Escuela / Educación
            lowerName.contains("school") || lowerName.contains("education") -> R.drawable.ic_education

            // Animales / Mascotas / Insectos
            lowerName.contains("animal") || lowerName.contains("pet") || lowerName.contains("insect") -> R.drawable.ic_checkmark

            // Naturaleza / Plantas / Montañas
            lowerName.contains("nature") || lowerName.contains("plant") || lowerName.contains("environment") ||
                    lowerName.contains("mountain") -> R.drawable.ic_nature

            // Tecnología / Computadoras / IA
            lowerName.contains("tech") || lowerName.contains("computer") || lowerName.contains("program") ||
                    lowerName.contains("artificial") || lowerName.contains("robot") || lowerName.contains("internet") -> R.drawable.ic_technology

            // Ciencia / Espacio
            lowerName.contains("science") || lowerName.contains("space") -> R.drawable.ic_rocket

            // Salud / Medicina
            lowerName.contains("health") || lowerName.contains("medic") || lowerName.contains("symptom") -> R.drawable.ic_health

            // Deportes / Fitness
            lowerName.contains("sport") || lowerName.contains("fit") || lowerName.contains("yoga") -> R.drawable.ic_sports

            // Hogar / Casa
            lowerName.contains("home") || lowerName.contains("house") || lowerName.contains("bathroom") -> R.drawable.ic_home_filled

            // Ciudad / Direcciones
            lowerName.contains("city") || lowerName.contains("direction") -> R.drawable.ic_city

            // Clima / Desastres
            lowerName.contains("weather") -> R.drawable.ic_cloud

            // Arte / Fotografía / Colores
            lowerName.contains("art") || lowerName.contains("color") || lowerName.contains("photo") -> R.drawable.ic_movie

            // Música / Películas / Juegos
            lowerName.contains("music") -> R.drawable.ic_music_note
            lowerName.contains("movie") || lowerName.contains("tv") -> R.drawable.ic_movie
            lowerName.contains("game") -> R.drawable.ic_emoji_events

            // Emociones / Psicología
            lowerName.contains("emotion") || lowerName.contains("feeling") || lowerName.contains("psychology") -> R.drawable.ic_mood

            // Compras / Ropa
            lowerName.contains("shop") -> R.drawable.ic_shopping
            lowerName.contains("clothes") -> R.drawable.ic_person

            // Ley / Gobierno / Política
            lowerName.contains("law") || lowerName.contains("politic") || lowerName.contains("government") -> R.drawable.ic_politician

            // Tiempo / Días
            lowerName.contains("time") -> R.drawable.ic_history

            // Misceláneo / Gramática
            lowerName.contains("verb") || lowerName.contains("adjective") || lowerName.contains("adverb") -> R.drawable.ic_bolt

            // Default
            else -> R.drawable.ic_label
        }
    }
}
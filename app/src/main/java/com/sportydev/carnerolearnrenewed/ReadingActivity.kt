package com.sportydev.carnerolearnrenewed

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReadingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reading)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupButtons()
        setupBottomNavigation()
    }

    private fun setupButtons() {
        // --- 1. Botón del Hero (Recommended) ---
        findViewById<FloatingActionButton>(R.id.btnHeroRead).setOnClickListener {
            openStory(
                "The Secret Garden",
                STORY_SECRET_GARDEN, // Texto largo definido abajo
                "Advanced"
            )
        }

        // --- 2. Lista de Historias ---

        // Beginner: The Lost Key
        findViewById<MaterialButton>(R.id.btnStart1).setOnClickListener {
            openStory(
                "The Lost Key",
                STORY_LOST_KEY,
                "Beginner"
            )
        }

        // Intermediate: A Day in the Park
        findViewById<MaterialButton>(R.id.btnStart2).setOnClickListener {
            openStory(
                "A Day in the Park",
                STORY_PARK,
                "Intermediate"
            )
        }

        // Advanced: The Secret Garden (Botón de la lista)
        findViewById<MaterialButton>(R.id.btnStart3).setOnClickListener {
            openStory(
                "The Secret Garden",
                STORY_SECRET_GARDEN,
                "Advanced"
            )
        }
    }

    // Función para navegar y pasar datos
    private fun openStory(title: String, content: String, level: String) {
        val intent = Intent(this, ReadingDetailActivity::class.java)
        intent.putExtra("STORY_TITLE", title)
        intent.putExtra("STORY_CONTENT", content)
        intent.putExtra("STORY_LEVEL", level)
        startActivity(intent)
    }

    private fun setupBottomNavigation() {
        findViewById<LinearLayout>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.nav_study).setOnClickListener {
            startActivity(Intent(this, StudyBookActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.nav_vocabulary).setOnClickListener {
            startActivity(Intent(this, VocabularyActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.nav_listening).setOnClickListener {
            startActivity(Intent(this, ListeningActivity::class.java))
            overridePendingTransition(0, 0)
        }
        // Ya estamos en Reading
    }

    // --- DATOS DE LAS HISTORIAS (HARDCODED TEMPORALMENTE) ---
    companion object {
        const val STORY_LOST_KEY = """
Once upon a time, there was a little boy named Tom. Tom liked to play in the forest near his house. One day, he found a small, old box under a tree. The box was locked.

Tom wanted to open the box, but he didn't have a key. He looked everywhere. He looked under the rocks. He looked inside the flowers. He even looked in his shoes! But the key was not there.

Sad, Tom went home. His grandmother saw him and asked, "Why are you sad, Tom?" Tom showed her the box. His grandmother smiled. She went to her room and came back with a shiny gold key. "This key belonged to your grandfather," she said.

Tom tried the key. Click! The box opened. Inside, there was a picture of Tom's grandfather when he was a little boy, playing under the same tree.
        """

        const val STORY_PARK = """
It was a beautiful Sunday morning. The sun was shining, and the birds were singing. Sarah decided to go to Central Park to relax after a long week of work. She took her favorite book and a blanket.

When she arrived, the park was full of people. Some were running, others were riding bicycles, and families were having picnics on the grass. Sarah found a quiet spot under a large oak tree. She spread her blanket and started to read.

Suddenly, a golden retriever puppy ran towards her and jumped on her blanket. "I am so sorry!" shouted a man running after the dog. Sarah laughed and petted the puppy. "It's okay, he is very cute," she said. The man introduced himself as Mike. They started talking about dogs, books, and life in the city. 

By the time the sun started to set, Sarah realized she hadn't read a single page of her book, but she had made a new friend.
        """

        const val STORY_SECRET_GARDEN = """
Mary Lennox was a sour-faced little girl who lived in India. When her parents died, she was sent to live with her uncle in a huge, mysterious manor in England. The house had a hundred rooms, most of them closed and locked.

But the most mysterious thing was the garden. Mary heard rumors of a secret walled garden that had been locked up for ten years. No one was allowed to enter, and the key was buried somewhere.

One day, with the help of a robin redbreast, Mary found the key buried near an ivy-covered wall. Her heart pounded in her chest as she put the key in the old lock. It turned with difficulty. She pushed the heavy door open and stepped inside.

The garden was overgrown with weeds and brown vines, but Mary saw tiny green shoots poking through the earth. The garden was not dead; it was sleeping. Mary decided then and there that she would bring the secret garden back to life.
        """
    }
}
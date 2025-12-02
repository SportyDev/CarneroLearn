package com.sportydev.carnerolearnrenewed

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListeningActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listening)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAudioCards()
        setupBottomNavigation()
    }

    private fun setupAudioCards() {
        // --- HERO CARD (Daily Podcast) ---
        findViewById<FloatingActionButton>(R.id.btnHeroPlay).setOnClickListener {
            openPlayer(
                "Mystery of the Ocean",
                "Advanced • Episode 12",
                "The ocean covers more than 70 percent of our planet, yet we have explored less than 5 percent of it. Today, we dive deep into the Marianas Trench to discover creatures that glow in the dark and survive extreme pressure..."
            )
        }

        // ==========================================
        // SECCIÓN BEGINNER
        // ==========================================

        // 1. A Day in the Life
        findViewById<ImageView>(R.id.btnPlayBeginner1).setOnClickListener {
            openPlayer(
                "A Day in the Life",
                "Beginner • 2:30 min",
                "I wake up at 7 AM every morning. First, I brush my teeth and wash my face. Then, I have breakfast. I usually eat toast and drink coffee. After breakfast, I take the bus to work. My job is very interesting..."
            )
        }

        // 2. Ordering Food
        findViewById<ImageView>(R.id.btnPlayBeginner2).setOnClickListener {
            openPlayer(
                "Ordering Food",
                "Beginner • 1:50 min",
                "Waiter: Hello, are you ready to order?\nCustomer: Yes, please. I would like the chicken salad.\nWaiter: Anything to drink?\nCustomer: Just water, please.\nWaiter: Certainly."
            )
        }

        // 3. Movie Review
        findViewById<ImageView>(R.id.btnPlayBeginner3).setOnClickListener {
            openPlayer(
                "Movie Review",
                "Beginner • 4:20 min",
                "Yesterday I went to the cinema to see the new superhero movie. It was fantastic! The special effects were amazing, and the story was very exciting. I really liked the main actor. I recommend it to everyone."
            )
        }

        // ==========================================
        // SECCIÓN INTERMEDIATE
        // ==========================================

        // 4. My Last Vacation
        findViewById<ImageView>(R.id.btnPlayIntermediate1).setOnClickListener {
            openPlayer(
                "My Last Vacation",
                "Intermediate • 3:15 min",
                "Last summer, I traveled to Italy with my family. It was a wonderful experience. We visited Rome, Florence, and Venice. The food was absolutely delicious, especially the authentic pizza and gelato. The weather was hot, but we enjoyed walking around the historic streets."
            )
        }

        // 5. Flight Announcements (At the Airport)
        findViewById<ImageView>(R.id.btnPlayIntermediate2).setOnClickListener {
            openPlayer(
                "Flight Announcements",
                "Intermediate • 3:10 min",
                "Ladies and gentlemen, welcome aboard Flight 405 with service to London Heathrow. We ask that you please fasten your seatbelts at this time and secure all baggage underneath your seat or in the overhead compartments. We will be taking off shortly."
            )
        }

        // ==========================================
        // SECCIÓN ADVANCED
        // ==========================================

        // 6. Technology Impact
        findViewById<ImageView>(R.id.btnPlayAdvanced1).setOnClickListener {
            openPlayer(
                "Technology Impact",
                "Advanced • 5:00 min",
                "In recent decades, technology has revolutionized the way we communicate and work. While the benefits of instant connectivity are undeniable, some experts argue that it has led to a decrease in face-to-face social skills and an increase in anxiety levels among teenagers."
            )
        }

        // 7. Job Interview Tips
        findViewById<ImageView>(R.id.btnPlayAdvanced2).setOnClickListener {
            openPlayer(
                "Job Interview Tips",
                "Advanced • 6:45 min",
                "Interviewer: Can you tell me about a time you faced a challenge at work?\nCandidate: Certainly. In my previous role, we had a tight deadline for a project. I organized a team meeting to prioritize tasks, and by delegating effectively, we managed to finish two days early."
            )
        }
    }

    private fun openPlayer(title: String, level: String, transcript: String) {
        val intent = Intent(this, ListeningDetailActivity::class.java)
        intent.putExtra("AUDIO_TITLE", title)
        intent.putExtra("AUDIO_LEVEL", level)
        intent.putExtra("AUDIO_TRANSCRIPT", transcript)
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

        // Listening (Ya estamos aquí)

        findViewById<LinearLayout>(R.id.nav_reading).setOnClickListener {
            startActivity(Intent(this, ReadingActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }
}
package com.sportydev.carnerolearnrenewed

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListeningDetailActivity : AppCompatActivity() {

    private var isPlaying = false
    private var progressStatus = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var seekBar: SeekBar
    private lateinit var tvCurrentTime: TextView
    private lateinit var btnPlayPause: FloatingActionButton

    // Texto que se leerá
    private var transcriptText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listening_detail)

        // 1. Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }

        // 2. Recibir Datos
        val title = intent.getStringExtra("AUDIO_TITLE") ?: "Unknown"
        val level = intent.getStringExtra("AUDIO_LEVEL") ?: "General"
        transcriptText = intent.getStringExtra("AUDIO_TRANSCRIPT") ?: "No transcript available."

        // 3. Setup UI
        findViewById<TextView>(R.id.tvAudioTitle).text = title
        findViewById<TextView>(R.id.tvAudioLevel).text = level
        findViewById<TextView>(R.id.tvTranscript).text = transcriptText

        seekBar = findViewById(R.id.seekBar)
        tvCurrentTime = findViewById(R.id.tvCurrentTime)
        btnPlayPause = findViewById(R.id.btnPlayPause)

        // Inicializar TTS
        TtsManager.initialize(this)

        // 4. Lógica de Play/Pause
        btnPlayPause.setOnClickListener {
            if (isPlaying) {
                pauseAudio()
            } else {
                playAudio()
            }
        }
        val fabQuiz = findViewById<ExtendedFloatingActionButton>(R.id.btnStartQuiz)

        fabQuiz.setOnClickListener {
            // Obtenemos el título de la historia actual
            val title = intent.getStringExtra("AUDIO_TITLE") ?: "Unknown"

            // Lanzamos el Quiz pasándole ese título
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("QUIZ_TOPIC", title)
            startActivity(intent)
            // Ejemplo de cómo sería el Intent futuro:
            // val intent = Intent(this, QuizActivity::class.java)
            // intent.putExtra("QUIZ_TYPE", "LISTENING")
            // intent.putExtra("CONTENT_ID", title)
            // startActivity(intent)
        }
    }

    private fun playAudio() {
        isPlaying = true
        // Cambiar icono a Pausa (usa un icono estándar si no tienes uno propio)
        btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)

        // Empezar a hablar
        TtsManager.speak(transcriptText)

        // Simular progreso de la barra
        startSeekBarUpdate()
    }

    private fun pauseAudio() {
        isPlaying = false
        // Cambiar icono a Play
        btnPlayPause.setImageResource(R.drawable.ic_play) // O android.R.drawable.ic_media_play

        // Detener audio
        TtsManager.stop()
    }

    // Simulación simple de la barra de progreso
    private fun startSeekBarUpdate() {
        Thread {
            while (isPlaying && progressStatus < 100) {
                progressStatus += 1

                // Actualizar UI
                handler.post {
                    seekBar.progress = progressStatus
                    // Simular tiempo (esto es fake, solo para efecto visual)
                    val minutes = progressStatus / 60
                    val seconds = progressStatus % 60
                    tvCurrentTime.text = String.format("%02d:%02d", minutes, seconds)
                }

                try {
                    // Ajusta este tiempo para que dure lo que dura el texto aprox
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            // Si termina
            if (progressStatus >= 100) {
                handler.post {
                    pauseAudio()
                    progressStatus = 0
                    seekBar.progress = 0
                    tvCurrentTime.text = "00:00"
                }
            }
        }.start()
    }

    override fun onDestroy() {
        TtsManager.stop()
        isPlaying = false
        super.onDestroy()
    }
}
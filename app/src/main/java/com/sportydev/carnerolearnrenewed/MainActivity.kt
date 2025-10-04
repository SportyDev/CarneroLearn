package com.sportydev.carnerolearnrenewed

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Actividad principal de la aplicación. Es la primera pantalla que ve el usuario al abrir la app.
 */
class MainActivity : BaseActivity() {

    /**
     * Método que se llama cuando la actividad es creada por primera vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita el modo "edge-to-edge" para que la UI ocupe toda la pantalla.
        enableEdgeToEdge()
        // Vincula esta clase de Kotlin con su archivo de diseño XML (activity_main.xml).
        setContentView(R.layout.activity_main)

        // Ajusta el padding de la vista raíz para evitar que el contenido se superponga con
        // las barras del sistema (como la barra de estado o de navegación).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Llama a la función que configura la barra de navegación.
        setupBottomNavigation()
    }

    /**
     * Inicializa y configura los listeners (eventos de clic) para la barra de navegación inferior.
     */
    private fun setupBottomNavigation() {
        // Botón de Inicio -> No realiza ninguna acción.
        findViewById<LinearLayout>(R.id.nav_home).setOnClickListener {
            // Ya estamos en la pantalla principal, por lo que no es necesario navegar.
        }

        // Botón de Estudio -> Abre la actividad StudyBookActivity.
        findViewById<LinearLayout>(R.id.nav_study).setOnClickListener {
            startActivity(Intent(this, StudyBookActivity::class.java))
            // Desactiva la animación por defecto al cambiar de actividad.
            overridePendingTransition(0, 0)
        }

        // Botón de Vocabulario -> Abre la actividad VocabularyActivity.
        findViewById<LinearLayout>(R.id.nav_vocabulary).setOnClickListener {
            startActivity(Intent(this, VocabularyActivity::class.java))
            overridePendingTransition(0, 0)
        }

        // Botón de Escucha (Listening) -> Abre la actividad ListeningActivity.
        findViewById<LinearLayout>(R.id.nav_listening).setOnClickListener {
            startActivity(Intent(this, ListeningActivity::class.java))
            overridePendingTransition(0, 0)
        }

        // Botón de Lectura (Reading) -> Abre la actividad ReadingActivity.
        findViewById<LinearLayout>(R.id.nav_reading).setOnClickListener {
            startActivity(Intent(this, ReadingActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }
}
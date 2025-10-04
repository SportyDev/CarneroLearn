package com.sportydev.carnerolearnrenewed

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Actividad que maneja la sección de "Listening" o escucha de la aplicación.
 */
class ListeningActivity : BaseActivity() {

    /**
     * Método principal que se ejecuta al crear la actividad. Es el punto de entrada.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita el modo de pantalla completa (edge-to-edge) para que la app ocupe todo el espacio.
        enableEdgeToEdge()

        // Establece el diseño visual de esta actividad, definido en el archivo R.layout.activity_listening.
        setContentView(R.layout.activity_listening)

        // Llama a la función para configurar la barra de navegación inferior.
        setupBottomNavigation()

        // Este listener ajusta los márgenes (padding) de la vista principal...
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // Obtiene las dimensiones de las barras del sistema (barra de estado, barra de navegación).
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Aplica esas dimensiones como padding para que el contenido no se oculte detrás de las barras.
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * Configura los listeners (eventos de clic) para los botones de la barra de navegación inferior.
     */
    private fun setupBottomNavigation() {
        // Botón de Inicio -> Abre la actividad principal (MainActivity).
        findViewById<LinearLayout>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            // Elimina la animación de transición entre actividades para un cambio de pantalla instantáneo.
            overridePendingTransition(0, 0)
        }

        // Botón de Estudio -> Abre la actividad del libro de estudio (StudyBookActivity).
        findViewById<LinearLayout>(R.id.nav_study).setOnClickListener {
            startActivity(Intent(this, StudyBookActivity::class.java))
            overridePendingTransition(0, 0)
        }

        // Botón de Vocabulario -> Abre la actividad de vocabulario (VocabularyActivity).
        findViewById<LinearLayout>(R.id.nav_vocabulary).setOnClickListener {
            startActivity(Intent(this, VocabularyActivity::class.java))
            overridePendingTransition(0, 0)
        }

        // Botón de Escucha (Listening) -> No realiza ninguna acción.
        findViewById<LinearLayout>(R.id.nav_listening).setOnClickListener {
            // Ya estamos en esta pantalla, por lo que no es necesario navegar a ningún lado.
        }

        // Botón de Lectura (Reading) -> Abre la actividad de lectura (ReadingActivity).
        findViewById<LinearLayout>(R.id.nav_reading).setOnClickListener {
            startActivity(Intent(this, ReadingActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }
}
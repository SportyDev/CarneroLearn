package com.sportydev.carnerolearnrenewed.ui.main

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.sportydev.carnerolearnrenewed.R
import com.sportydev.carnerolearnrenewed.ui.base.BaseActivity

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupButtons()
    }

    private fun setupButtons() {
        // 1. Botón Atrás
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // 2. Opción Editar Perfil
        findViewById<LinearLayout>(R.id.optionProfile).setOnClickListener {
            Toast.makeText(this, "Edit Profile feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        // 3. Switch Sonido
        val switchSound = findViewById<SwitchMaterial>(R.id.switchSound)
        switchSound.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) "Sound ON" else "Sound OFF"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // 4. Switch Recordatorios
        val switchReminder = findViewById<SwitchMaterial>(R.id.switchReminder)
        switchReminder.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) "Daily reminders enabled" else "Reminders disabled"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // 5. Botón Log Out
        findViewById<MaterialButton>(R.id.btnLogout).setOnClickListener {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()



            finish()
        }
    }
}
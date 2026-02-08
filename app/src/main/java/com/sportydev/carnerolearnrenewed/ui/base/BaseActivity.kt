package com.sportydev.carnerolearnrenewed.ui.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding

abstract class BaseActivity : AppCompatActivity() {

    /**
     * Las Activities hijas pueden sobrescribir este método para especificar
     * qué vista debe recibir el padding de la barra de estado.
     */
    protected open fun getPaddingTargetView(): View? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // 3. Lógica para el padding dinámico
        val targetView = getPaddingTargetView()
        if (targetView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(targetView) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.updatePadding(top = insets.top)
                WindowInsetsCompat.CONSUMED
            }
        }
    }
}
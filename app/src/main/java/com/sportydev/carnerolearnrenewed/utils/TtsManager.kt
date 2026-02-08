package com.sportydev.carnerolearnrenewed.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

object TtsManager : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isReady = false


    fun initialize(context: Context) {
        if (tts == null) {
            tts = TextToSpeech(context, this)
        }
    }


    fun speak(text: String, slow: Boolean = false) {
        if (isReady) {
            // Ajustar velocidad: 0.8f es normal-lento, 0.5f es muy lento para estudiar
            val rate = if (slow) 0.5f else 0.8f
            tts?.setSpeechRate(rate)

            tts?.language = Locale.US
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            Log.e("TtsManager", "TTS aún no está listo, intenta de nuevo en un momento.")
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isReady = false
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TtsManager", "El idioma Inglés no está soportado en este dispositivo.")
                isReady = false
            } else {
                isReady = true
                Log.i("TtsManager", "TTS inicializado correctamente.")
            }
        } else {
            isReady = false
            Log.e("TtsManager", "Fallo al inicializar TTS.")
        }
    }
}
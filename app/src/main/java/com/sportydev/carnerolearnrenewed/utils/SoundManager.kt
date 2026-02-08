package com.sportydev.carnerolearnrenewed.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.sportydev.carnerolearnrenewed.R

object SoundManager {

    private var soundPool: SoundPool? = null

    // IDs de los sonidos cargados en memoria
    private var correctSoundId: Int = 0
    private var wrongSoundId: Int = 0
    private var winSoundId: Int = 0   // Faltaba esto
    private var loseSoundId: Int = 0  // Faltaba esto

    private var isLoaded = false

    fun initialize(context: Context) {
        if (soundPool == null) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build()

            // Cargar los archivos (Asegúrate que existen en res/raw)
            correctSoundId = soundPool!!.load(context, R.raw.sfx_correct, 1)
            wrongSoundId = soundPool!!.load(context, R.raw.sfx_wrong, 1)

            // AGREGADOS: Cargar Win y Lose
            // Si tus archivos tienen otro nombre, cámbialo aquí (ej: R.raw.victory)
            winSoundId = soundPool!!.load(context, R.raw.sfx_win, 1)
            loseSoundId = soundPool!!.load(context, R.raw.sfx_lose, 1)

            soundPool?.setOnLoadCompleteListener { _, _, status ->
                if (status == 0) isLoaded = true
            }
        }
    }

    fun playCorrect() {
        if (isLoaded) soundPool?.play(correctSoundId, 1f, 1f, 1, 0, 1f)
    }

    fun playWrong() {
        if (isLoaded) soundPool?.play(wrongSoundId, 1f, 1f, 1, 0, 1f)
    }

    fun playWin() {
        if (isLoaded) soundPool?.play(winSoundId, 1f, 1f, 1, 0, 1f)
    }

    fun playLose() {
        if (isLoaded) soundPool?.play(loseSoundId, 1f, 1f, 1, 0, 1f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}
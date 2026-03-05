package org.example.project

import kotlinx.browser.document
import org.w3c.dom.HTMLAudioElement

actual class AudioPlayer actual constructor() {

    private val sounds = mutableMapOf<String, HTMLAudioElement>()

    actual fun playSound(name: String) {

        val fileName = when(name) {
            "drum" -> "drum.mp3"
            "guitar" -> "guitar.mp3"
            "sax" -> "sax.mp3"
            else -> return
        }

        val audio = sounds.getOrPut(fileName) {
            val element = document.createElement("audio") as HTMLAudioElement
            element.src = fileName
            element
        }

        audio.currentTime = 0.0
        audio.play()
    }
}
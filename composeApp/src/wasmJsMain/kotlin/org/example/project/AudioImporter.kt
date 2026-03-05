package org.example.project

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement

actual class AudioImporter {

    actual fun pickAudio(onResult: (String) -> Unit) {

        val input = document.createElement("input") as HTMLInputElement
        input.type = "file"
        input.accept = "audio/*"

        input.onchange = {

            val file = input.files?.item(0)

            file?.let {
                val url = js("URL.createObjectURL(file)") as String
                onResult(url)
            }

        }

        input.click()
    }
}
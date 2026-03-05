package org.example.project

actual class AudioImporter {

    actual fun pickAudio(onAudioPicked: (String) -> Unit) {

        val chooser = javax.swing.JFileChooser()
        val result = chooser.showOpenDialog(null)

        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            onAudioPicked(chooser.selectedFile.absolutePath)
        }

    }
}
package org.example.project


import android.app.Activity
import android.content.Intent
import android.provider.MediaStore


actual class AudioImporter {

    private var callback: ((String) -> Unit)? = null

    actual fun pickAudio(onAudioPicked: (String) -> Unit) {

        val activity = currentActivity ?: return
        callback = onAudioPicked

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "audio/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }

        activity.startActivityForResult(intent, 1001)
    }

    fun onActivityResult(requestCode: Int, data: Intent?) {

        if (requestCode == 1001) {

            val uri = data?.data ?: return
            callback?.invoke(uri.toString())

        }
    }

    companion object {
        var currentActivity: Activity? = null
    }
}
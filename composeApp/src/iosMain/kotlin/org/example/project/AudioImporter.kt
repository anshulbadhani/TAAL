package org.example.project

import platform.UIKit.*
import platform.UniformTypeIdentifiers.*
import platform.Foundation.*
import kotlinx.cinterop.*
import platform.darwin.NSObject

actual class AudioImporter {

    actual fun pickAudio(onResult: (String) -> Unit) {

        val picker = UIDocumentPickerViewController(
            forOpeningContentTypes = listOf(UTTypeAudio),
            asCopy = true
        )

        picker.delegate = object : NSObject(), UIDocumentPickerDelegateProtocol {

            override fun documentPicker(
                controller: UIDocumentPickerViewController,
                didPickDocumentsAtURLs: List<*>
            ) {

                val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL
                url?.path?.let { onResult(it) }
            }
        }

        val root =
            UIApplication.sharedApplication.keyWindow?.rootViewController

        root?.presentViewController(
            picker,
            animated = true,
            completion = null
        )
    }
}
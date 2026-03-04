package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberBeatEditorState(): BeatEditorState {

    return remember { BeatEditorState() }

}
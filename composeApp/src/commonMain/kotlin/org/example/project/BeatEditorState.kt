package org.example.project

import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class BeatEditorState {

    var grid by mutableStateOf(
        List(8) { MutableList(32) { false } }
    )

    fun toggle(row: Int, col: Int) {
        grid = grid.toMutableList().apply {
            this[row] = this[row].toMutableList().apply {
                this[col] = !this[col]
            }
        }
    }
}
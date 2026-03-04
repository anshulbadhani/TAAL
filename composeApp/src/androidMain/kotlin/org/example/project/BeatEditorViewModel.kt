package org.example.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class BeatEditorViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val KEY = "steps_state"

    private val _steps = MutableStateFlow(
        savedStateHandle.get<Map<Pair<Int, Int>, Boolean>>(KEY) ?: emptyMap()
    )

    val steps: StateFlow<Map<Pair<Int, Int>, Boolean>> = _steps

    fun toggleStep(instrumentIndex: Int, stepIndex: Int) {

        _steps.update { current ->

            val newMap = current.toMutableMap()
            val key = instrumentIndex to stepIndex

            newMap[key] = !(newMap[key] ?: false)

            savedStateHandle[KEY] = newMap

            newMap
        }
    }
}
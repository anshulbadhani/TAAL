package org.example.project

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TileView(
    tile: Tile,
    audioPlayer: AudioPlayer,
    onEdit: (Tile) -> Unit
) {

    var editorOpen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(90.dp)
            .background(tile.instrument.color, RoundedCornerShape(14.dp))
            .combinedClickable(

                onClick = {
                    audioPlayer.playSound(tile.instrument.name.lowercase())
                },

                onLongClick = {
                    editorOpen = true
                    onEdit(tile)
                }

            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            tile.instrument.name,
            color = Color.White
        )

    }


    if (editorOpen) {

        Dialog(
            onDismissRequest = { editorOpen = false }
        ) {

            AudioEditor(
                fileName = tile.beat?.fileName ?: tile.instrument.name,
                onClose = { editorOpen = false },
                onSave = { file ->
                    editorOpen = false
                    onEdit(tile)
                }
            )

        }
    }
}

@Composable
fun AudioEditor(
    fileName: String,
    onClose: () -> Unit,
    onSave: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .background(Color(0xFF1E1E1E), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(fileName, color = Color.White)

            Row {

                Text(
                    "Save",
                    color = Color.Green,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            onSave(fileName)
                        }
                )

                Text(
                    "Close",
                    color = Color.Red,
                    modifier = Modifier.clickable { onClose() }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        EditorToolbar()

        Spacer(Modifier.height(12.dp))

        WaveformView(fileName)

        Spacer(Modifier.height(12.dp))

        PlaybackControls()
    }
}

@Composable
fun EditorToolbar() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        EditorButton("Trim")
        EditorButton("Cut")
        EditorButton("Fade In")
        EditorButton("Fade Out")
        EditorButton("Normalize")

    }
}

@Composable
fun WaveformView(fileName: String) {

    val waveform = remember(fileName) {
        List(200) { Random.nextFloat() }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(Color.Black, RoundedCornerShape(10.dp))
    ) {

        val widthPerSample = size.width / waveform.size

        waveform.forEachIndexed { i, value ->

            val x = i * widthPerSample
            val amplitude = value * size.height / 2

            drawLine(
                color = Color.Cyan,
                start = Offset(x, size.height / 2 - amplitude),
                end = Offset(x, size.height / 2 + amplitude),
                strokeWidth = 2f
            )
        }
    }
}

@Composable
fun PlaybackControls() {

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text("Play", color = Color.White)
        Text("Pause", color = Color.White)
        Text("Stop", color = Color.White)

    }
}

@Composable
fun EditorButton(
    text: String,
    onClick: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF3A3A3A))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White
        )
    }
}





import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PianoRollEditor(
    onClose: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .background(Color(0xFF444444), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, null)
            }

            Row {

                IconButton(onClick = {}) {
                    Icon(Icons.Default.Save, null)
                }

                IconButton(onClick = {}) {
                    Icon(Icons.Default.Add, null)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        PianoKeyboard()

        Spacer(Modifier.height(12.dp))

        StepMatrix()
    }
}

@Composable
fun PianoKeyboard() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        repeat(14) {

            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(60.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
fun StepMatrix() {

    val horizontalScroll = rememberScrollState()
    val verticalScroll = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
            .horizontalScroll(horizontalScroll)
    ) {

        Column {

            repeat(24) { row ->

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    repeat(32) { column ->

                        val color =
                            if (row % 4 == 0)
                                Color(0xFFAA5555)
                            else
                                Color(0xFF444444)

                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(color, CircleShape)
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))
            }
        }
    }
}
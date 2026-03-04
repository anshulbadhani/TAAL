package org.example.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun BeatEditorScreen(
    categories: List<InstrumentCategory>,
    state: BeatEditorState,
    modifier: Modifier = Modifier
) {

    val horizontalScroll = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {

        LazyColumn(
            modifier = Modifier
                .width(90.dp)
                .fillMaxHeight()
                .background(Color(0xFF2F2F2F), RoundedCornerShape(16.dp))
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            itemsIndexed(categories) { _, category ->

                val instrument = category.tiles.first().instrument

                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(instrument.color),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(instrument.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            item {

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))


        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF3A3A3A), RoundedCornerShape(16.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            itemsIndexed(categories) { instrumentIndex, category ->

                val instrument = category.tiles.first().instrument

                Row(
                    modifier = Modifier
                        .horizontalScroll(horizontalScroll)
                        .height(70.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    repeat(32) { stepIndex ->

                        val active = state.grid[instrumentIndex][stepIndex]

                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (active)
                                        instrument.color
                                    else
                                        Color(0xFF555555)
                                )
                                .clickable {
                                    state.toggle(instrumentIndex, stepIndex)
                                }
                        )
                    }
                }
            }
        }
    }
}
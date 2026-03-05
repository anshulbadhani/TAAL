package org.example.project

import PianoRollEditor
import TileViewModel
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import org.example.project.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.time.Clock


@Composable
@Preview
fun App() {

    var currentScreen by rememberSaveable { mutableStateOf("standards") }
    val beatEditorState = rememberBeatEditorState()
    val audioImporter = remember { AudioImporter() }
    val tileViewModel = remember { TileViewModel() }


    MaterialTheme {

        when (currentScreen) {
            "standards" -> {

                StandardsScreen(
                    onNavigateToProjects = { currentScreen = "projects" }
                )
            }
            "projects" -> {

                ProjectSelectionScreen(
                    onNavigateToMusic = { currentScreen = "music_pad" },
                    onNavigateBack = { currentScreen = "standards" }
                )
            }
            "music_pad" -> {

                MusicPadScreen(
                    state = beatEditorState,
                    onNavigateBack = { currentScreen = "projects" },
                    audioImporter = audioImporter,
                    tileViewModel = tileViewModel
                )
            }
        }
    }
}


@Composable
fun MusicPadScreen(
    state: BeatEditorState,
    onNavigateBack: () -> Unit,
    audioImporter: AudioImporter,
    tileViewModel: TileViewModel
) {

    var showAudioEditor by remember { mutableStateOf(false) }

    var isEditorMode by remember { mutableStateOf(false) }

    var showBeatSelector by remember { mutableStateOf(false) }
    var showPianoEditor by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedTile by remember { mutableStateOf<Tile?>(null) }

    val beats = remember {
        listOf(
            Beat("b1", "Classic Beat", "drum.wav"),
            Beat("b2", "Rock Beat", "rock.wav"),
            Beat("b3", "Jazz Beat", "jazz.wav")
        )
    }

    val audioPlayer = remember { AudioPlayer() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {


            TopBar(onBackClick = onNavigateBack)

            Spacer(Modifier.height(16.dp))

            if (isEditorMode) {

                SoundGrid(
                    categories = tileViewModel.categories,
                    audioPlayer = audioPlayer,
                    modifier = Modifier.weight(1f),
                    onLongPress = { categoryTitle, tile ->

                        selectedCategory = categoryTitle
                        selectedTile = tile

                        if (tile.instrument.name == "piano") {
                            showPianoEditor = true
                        } else {
                            showBeatSelector = true
                        }
                    }
                )

            } else {

                BeatEditorScreen(
                    categories = tileViewModel.categories,
                    state = state,
                    modifier = Modifier.weight(1f),
                    onTileLongPress = { instrumentIndex, stepIndex ->

                        val category = tileViewModel.categories[instrumentIndex]
                        val tile = category.tiles[stepIndex]
                        selectedCategory = category.title
                        selectedTile = tile

                        if (tile.instrument.name == "piano") {
                            showPianoEditor = true
                        } else {
                            showAudioEditor = true
                        }
                    }
                )
            }
        }

        BottomControls(
            isEditorMode = isEditorMode,
            onToggle = { isEditorMode = !isEditorMode },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )

        if (showBeatSelector && selectedTile != null) {
            Dialog(onDismissRequest = { showBeatSelector = false }) {

                Column(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {

                    BeatSelector(
                        beats = beats,
                        audioPlayer = audioPlayer,
                        onSaveToTile = { beat ->
                            tileViewModel.assignBeat(
                                selectedCategory!!,
                                selectedTile!!.id,
                                beat
                            )
                            showBeatSelector = false
                        },
                        onCreateNewTile = { beat ->
                            tileViewModel.addTile(
                                selectedCategory!!,
                                selectedTile!!,
                                beat
                            )
                            showBeatSelector = false
                        },

                        onImportAudio = {
                            audioImporter.pickAudio { path ->

                                tileViewModel.assignBeat(
                                    selectedCategory!!,
                                    selectedTile!!.id,
                                    Beat(
                                        id = "imported",
                                        name = "Imported Audio",
                                        fileName = path
                                    )
                                )

                                showBeatSelector = false
                            }
                        },

                        onDismiss = { showBeatSelector = false }
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "+ Import Audio",
                        color = Color.Blue,
                        modifier = Modifier.clickable {

                            audioImporter.pickAudio { path ->

                                tileViewModel.assignBeat(
                                    selectedCategory!!,
                                    selectedTile!!.id,
                                    Beat(
                                        id = "imported_${Clock.System.now().toEpochMilliseconds()}",
                                        name = "Imported",
                                        fileName = path
                                    )
                                )

                                showBeatSelector = false
                            }

                        }
                    )
                }
            }
        }


        if (showPianoEditor && selectedTile != null) {
            Dialog(onDismissRequest = { showPianoEditor = false }) {

                PianoRollEditor(
                    onClose = { showPianoEditor = false }
                )
            }
        }

        if (showAudioEditor && selectedTile != null) {
            Dialog(onDismissRequest = { showAudioEditor = false }) {

                AudioEditor(
                    fileName = selectedTile!!.beat?.fileName
                        ?: selectedTile!!.instrument.name,

                    onClose = { showAudioEditor = false },

                    onSave = { fileName ->

                        tileViewModel.assignBeat(
                            selectedCategory!!,
                            selectedTile!!.id,
                            Beat("edited", "Edited Beat", fileName)
                        )

                        showAudioEditor = false
                    }
                )
            }
        }
    }
}


@Composable
fun TopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){

        IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        Box(
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
        ) {
            Text(
                text = "00:00:00",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
        Row {
            IconButton({}) { Icon(Icons.Default.Mic, null, tint = Color.White) }
            IconButton({}) { Icon(
                imageVector = Icons.Default.Speed,
                contentDescription = "Metronome",
                tint = Color.White
            ) }
            IconButton({}) { Icon(Icons.Default.VolumeUp, null, tint = Color.White) }
            IconButton({}) { Icon(Icons.Default.Menu, null, tint = Color.White) }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SoundGrid(
    categories: List<InstrumentCategory>,
    audioPlayer: AudioPlayer,
    modifier: Modifier = Modifier,
    onLongPress: (String, Tile) -> Unit
){

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        items(categories.size) { categoryIndex ->

            val category = categories[categoryIndex]

            Column {

                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyHorizontalGrid(
                    rows = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {

                    items(category.tiles.size) { index ->

                        val tile = category.tiles[index]

                        SoundPad(
                            color = tile.instrument.color,
                            icon = painterResource(tile.instrument.iconRes),
                            onClick = {
                                tile.beat?.let {
                                    audioPlayer.playSound(it.fileName)
                                } ?: audioPlayer.playSound(tile.instrument.name)
                            },
                            onLongPress = {
                                onLongPress(category.title, tile)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SoundPad(
    color: Color,
    icon: Painter,
    onClick: () -> Unit,
    onLongPress: () -> Unit
){
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        label = ""
    )

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(20.dp))
            .background(color)
            .combinedClickable(
                onClick = {
                    pressed = true
                    onClick()
                },
                onLongClick = {
                    onLongPress()
                }
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }

    LaunchedEffect(pressed) {
        if (pressed) {
            delay(80)
            pressed = false
        }
    }
}

@Composable
fun BottomControls(
    isEditorMode: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = BottomBarColor,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {

            Icon(
                imageVector = Icons.Default.GraphicEq,
                contentDescription = null,
                tint = if (!isEditorMode) Color.White else Color.Gray,
                modifier = Modifier.clickable { onToggle() }
            )

            Icon(
                imageVector = Icons.Default.Piano,
                contentDescription = null,
                tint = if (isEditorMode) Color.White else Color.Gray,
                modifier = Modifier.clickable { onToggle() }
            )
        }
    }
}
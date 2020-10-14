package com.darklabs.guessgame

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.darklabs.guessgame.ui.GuessGameTheme
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

@ExperimentalLayout
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuessGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    GuessGameTheme {
                        GameBoard()
                    }
                }
            }
        }
    }
}

@ExperimentalLayout
@Preview(showBackground = true)
@Composable
fun GameBoard() {

    val rState = remember { mutableStateOf(127f) }
    val gState = remember { mutableStateOf(127f) }
    val bState = remember { mutableStateOf(127f) }

    val dialogState = remember { mutableStateOf(false) }

    val rTarget = remember { 127.0 }
    val gTarget = remember { 127.0 }
    val bTarget = remember { 127.0 }

    fun calculateScore(): Int {
        val rDiff = rState.value - rTarget
        val gDiff = gState.value - gTarget
        val bDiff = bState.value - bTarget

        val diff = sqrt(rDiff.pow(2.0) + gDiff.pow(2.0) + bDiff.pow(2.0))
        return ((255.0 - diff) / 10.0).toInt()
    }

    ScoreDialog(showingState = dialogState, data = calculateScore())

    Column {
        Row(modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.weight(weight = 1f),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Target Color Block")
                Box(
                        modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.9f)
                                .background(color = Color(red = rTarget.toInt(),
                                        green = gTarget.toInt(),
                                        blue = bTarget.toInt())),
                )
                Text(text = "Match this color", modifier = Modifier.weight(0.1f))

            }

            Spacer(modifier = Modifier.preferredWidth(5.dp))

            Column(
                    modifier = Modifier.weight(weight = 1f),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Guess Color Block")
                Box(
                        modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.9f)
                                .background(color = Color(red = rState.value.toInt(),
                                        green = gState.value.toInt(),
                                        blue = bState.value.toInt())),
                )
                Row(modifier = Modifier.fillMaxWidth().weight(0.1f),
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(text = "R : ${rState.value.toInt()}")
                    Text(text = "G : ${gState.value.toInt()}")
                    Text(text = "B : ${bState.value.toInt()}")
                }
            }
        }

        Spacer(modifier = Modifier.preferredHeight(5.dp))

        Button(onClick = {
            dialogState.value = true
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Hit Me !")
        }
        ColorSlider(state = rState, color = Color.Red)
        ColorSlider(state = gState, color = Color.Green)
        ColorSlider(state = bState, color = Color.Blue)
    }

}


@Composable
fun ScoreDialog(showingState: MutableState<Boolean>, data: Int) {
    if (showingState.value) {
        AlertDialog(
                onDismissRequest = {
                    showingState.value = false
                },
                text = {
                    Text(text = "$data")
                },
                title = {
                    Text(text = "Your Score")
                },
                buttons = {
                    Row(horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Close",
                                modifier = Modifier.padding(16.dp).clickable(onClick = {
                                    showingState.value = false
                                }))
                    }
                })
    }
}

@Composable
fun ColorSlider(state: MutableState<Float>, color: Color) {
    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment
            .CenterVertically) {
        Text(text = "0", color = color)
        Slider(
                modifier = Modifier.weight(1f),
                value = state.component1(),
                onValueChange = state.component2(),
                valueRange = 0f..255f
        )
        Text(text = "255", color = color)
    }
}
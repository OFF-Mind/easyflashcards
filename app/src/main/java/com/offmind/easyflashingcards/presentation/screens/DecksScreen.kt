package com.offmind.easyflashingcards.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.offmind.easyflashingcards.R
import com.offmind.easyflashingcards.domain.model.Deck
import com.offmind.easyflashingcards.presentation.AppBarSettings
import com.offmind.easyflashingcards.presentation.viewmodel.DecksViewModel
import com.offmind.easyflashingcards.ui.theme.DisabledTurquoise
import com.offmind.easyflashingcards.ui.theme.MainTurquoise
import org.koin.androidx.compose.koinViewModel

@Composable
fun DecksScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    appBarSettings: MutableState<AppBarSettings>,
    decksViewModel: DecksViewModel = koinViewModel()
) {

    val state by decksViewModel.state.collectAsState()
    appBarSettings.value = appBarSettings.value.copy(title = state.title, showHomeButton = true)

    DrawScreen(
        state = state,
        paddingValues = paddingValues
    ) { route ->
        navController.navigate(route = route)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawScreen(
    state: DecksViewModel.DeckScreenState,
    paddingValues: PaddingValues,
    onNavigateNext: (route: String) -> Unit
) {
    when (state) {
        is DecksViewModel.DeckScreenState.ShowDecks -> {
            Column(modifier = Modifier.padding(paddingValues)) {
                DecksList(
                    state.decks, false,
                    onDeckSelected = {
                        onNavigateNext.invoke("cardsList/${it}")
                    },
                    onNewDeckSelected = {
                        //no-op
                    })
            }
        }

        else -> {}
    }
}

@ExperimentalMaterial3Api
@Composable
fun CreateNewDeck(
    onCreate: (deckName: String) -> Unit
) {
    var newDeckName by remember {
        mutableStateOf("")
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(200.dp)
                .background(
                    color = Color(0xFF476E99),
                    shape = RoundedCornerShape(5.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            TextField(value = newDeckName, onValueChange = {
                newDeckName = it
            })
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = { onCreate.invoke(newDeckName) }) {
            Text(text = "Create")
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun DecksList(
    availableDecks: List<Deck>,
    showAddNewDeck: Boolean,
    onDeckSelected: (deckId: Int) -> Unit,
    onNewDeckSelected: (newDeckName: String) -> Unit
) {
    LazyColumn {
        if (showAddNewDeck) {
            item {
                NewItem {
                    onNewDeckSelected.invoke(it)
                }
            }
        }

        availableDecks.forEach {
            item {
                Surface(
                    shadowElevation = 5.dp,
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .padding(10.dp)

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clickable { onDeckSelected.invoke(it.id) },
                        verticalAlignment = CenterVertically
                    )
                    {
                        Spacer(modifier = Modifier.width(13.dp))
                        Text(text = it.displayName, fontSize = 25.sp, color = Color(0xFF313133))
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .background(color = MainTurquoise)
                                .fillMaxHeight()
                                .width(35.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                imageVector = ImageVector.run { vectorResource(id = R.drawable.deck_arrow) },
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun NewItem(onCreateNewDeck: (deckName: String) -> Unit) {
    var canCreate by remember {
        mutableStateOf(false)
    }
    var newDeckName by remember {
        mutableStateOf("")
    }
    Surface(
        shadowElevation = 5.dp,
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable {
                    if (canCreate) {
                        onCreateNewDeck.invoke(newDeckName)
                    }
                }
        ) {
            Image(
                imageVector = ImageVector.run { vectorResource(id = R.drawable.new_deck_flag) },
                contentDescription = ""
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(80.dp)
                        .padding(horizontal = 40.dp, vertical = 10.dp)
                ) {
                    TextField(
                        value = newDeckName,
                        label = {
                            Text(text = "New deck name")
                        },
                        shape = TextFieldDefaults.outlinedShape,
                        onValueChange = {
                            newDeckName = it
                            canCreate = newDeckName.isNotEmpty()
                        })
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .background(color = if (canCreate) MainTurquoise else DisabledTurquoise)
                        .fillMaxHeight()
                        .width(35.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = ImageVector.run { vectorResource(id = R.drawable.deck_arrow) },
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

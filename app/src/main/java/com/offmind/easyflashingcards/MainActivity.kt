package com.offmind.easyflashingcards

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.offmind.easyflashingcards.domain.repository.CardsRepository
import com.offmind.easyflashingcards.domain.usecase.ReadCardsCsvUseCase
import com.offmind.easyflashingcards.presentation.AppBarSettings
import com.offmind.easyflashingcards.presentation.Empty
import com.offmind.easyflashingcards.presentation.NavigationRoutes
import com.offmind.easyflashingcards.presentation.screens.DeckCardsScreen
import com.offmind.easyflashingcards.presentation.screens.DecksScreen
import com.offmind.easyflashingcards.presentation.screens.DisplayHomeScreen
import com.offmind.easyflashingcards.presentation.screens.FlashCardScreen
import com.offmind.easyflashingcards.presentation.screens.ImportCardsScreen
import com.offmind.easyflashingcards.ui.theme.EasyFlashingCardsTheme
import com.offmind.easyflashingcards.ui.theme.MainTurquoise
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val exportUri = remember { getExportUri(intent) }
            EasyFlashingCardsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val appBarSettings = remember { mutableStateOf(Empty) }

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(text = appBarSettings.value.title) },
                                navigationIcon = {
                                    if (appBarSettings.value.showHomeButton) {
                                        Image(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow),
                                            contentDescription = "",
                                            colorFilter = ColorFilter.tint(MainTurquoise),
                                            modifier = Modifier
                                                .padding(horizontal = 10.dp)
                                                .clickable {
                                                    navController.popBackStack()
                                                })
                                    }
                                })
                        },
                        content = { paddingValues ->
                            Box(modifier = Modifier.fillMaxSize()) {
                                NavigationComponent(
                                    navController = navController,
                                    paddingValues = paddingValues,
                                    appBarSettings = appBarSettings
                                )
                            }
                        }
                    )

                    if (exportUri != null) {
                        exportAndKeepCards(exportUri) {
                            navController.navigate("importDecks")
                        }
                    }
                }
            }
        }
    }

    private fun exportAndKeepCards(path: Uri, onComplete: () -> Unit) {
        val exportUseCase: ReadCardsCsvUseCase by inject()
        val cardsRepo: CardsRepository by inject()
        lifecycleScope.launch {
            cardsRepo.clearTempDeck()
            cardsRepo.writeCards(
                exportUseCase.invoke(path).map { it.copy(deckId = -10) })
            onComplete.invoke()
        }
    }

    private fun getExportUri(intent: Intent?) = when (intent?.action) {
        Intent.ACTION_SEND -> {
            if ("text/comma-separated-values" == intent.type) {
                intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri
            } else {
                null
            }
        }

        else -> {
            null
        }
    }

    @Composable
    fun NavigationComponent(
        navController: NavHostController,
        paddingValues: PaddingValues,
        appBarSettings: MutableState<AppBarSettings>
    ) {
        NavHost(navController = navController, startDestination = "home") {
            composable(NavigationRoutes.HomeScreenRoute.route) {
                DisplayHomeScreen(
                    navController = navController,
                    appBarSettings = appBarSettings
                )
            }
            composable(NavigationRoutes.DecksScreenRoute.route) {
                DecksScreen(
                    paddingValues = paddingValues,
                    navController = navController,
                    appBarSettings = appBarSettings
                )
            }
            composable(NavigationRoutes.ImportDecksScreenRoute.route) {
                ImportCardsScreen(
                    paddingValues = paddingValues,
                    navController = navController,
                    appBarSettings = appBarSettings)
            }
            composable(
                NavigationRoutes.CardsListScreen.route,
                arguments = listOf(navArgument("deckId") { type = NavType.IntType })
            ) {
                DeckCardsScreen(
                    navController = navController,
                    appBarSettings = appBarSettings,
                    paddingValues = paddingValues
                )
            }
            composable(
                NavigationRoutes.CardFlashScreen.route
            ) {
                FlashCardScreen(
                    paddingValues = paddingValues,
                    appBarSettings = appBarSettings,
                    navController = navController
                )
            }
        }
    }
}
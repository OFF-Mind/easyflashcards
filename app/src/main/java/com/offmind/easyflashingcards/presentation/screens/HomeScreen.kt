package com.offmind.easyflashingcards.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.offmind.easyflashingcards.R
import com.offmind.easyflashingcards.presentation.AppBarSettings
import com.offmind.easyflashingcards.presentation.NavigationRoutes
import com.offmind.easyflashingcards.presentation.viewmodel.HomeViewModel
import com.offmind.easyflashingcards.ui.theme.DarkWhite
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DisplayHomeScreen(
    navController: NavController,
    appBarSettings: MutableState<AppBarSettings>,
    homeViewModel: HomeViewModel = koinViewModel()
) {

    val state by homeViewModel.state.collectAsState()
    appBarSettings.value = appBarSettings.value.copy(title = state.title, showHomeButton = false)

    when (state) {
        is HomeViewModel.HomeViewModelState.Idle -> MenuContainer {
            when (it) {
                MenuOption.DECKS -> navController.navigate(NavigationRoutes.DecksScreenRoute.route)
                MenuOption.START -> navController.navigate(NavigationRoutes.CardFlashScreen.route)
                else -> {}
            }
        }
    }
}

@Composable
private fun MenuContainer(
    onSelectMenuOption: (option: MenuOption) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            MainMenuButton(
                size = MainMenuButtonSize.MEDIUM,
                icon = R.drawable.start_icon,
                text = "Start"
            ) {
                onSelectMenuOption.invoke(MenuOption.START)
            }
            Spacer(modifier = Modifier.width(10.dp))
            MainMenuButton(
                size = MainMenuButtonSize.SMALL,
                icon = R.drawable.decks_icon,
                text = "Decks"
            ) {
                onSelectMenuOption.invoke(MenuOption.DECKS)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            MainMenuButton(
                size = MainMenuButtonSize.SMALL,
                icon = R.drawable.settings_icon,
                text = "Settings"
            ) {
                //TODO
            }
            Spacer(modifier = Modifier.width(10.dp))
            MainMenuButton(
                size = MainMenuButtonSize.MEDIUM,
                icon = R.drawable.statistics_icon,
                text = "Statistics"
            ) {
                //TODO
            }
        }
    }
}

@Composable
private fun MainMenuButton(
    size: MainMenuButtonSize,
    icon: Int,
    text: String,
    onSelect: () -> Unit
) {
    Surface(shadowElevation = 7.dp, shape = RoundedCornerShape(4.dp)) {
        Column(modifier = Modifier
            .width(size.size)
            .height(140.dp)
            .background(DarkWhite)
            .clickable { onSelect.invoke() }
            .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.weight(1f))
            Image(imageVector = ImageVector.vectorResource(id = icon), contentDescription = "")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = text)
        }
    }
}

private enum class MainMenuButtonSize(val size: Dp) {
    SMALL(100.dp),
    MEDIUM(200.dp),
    WIDE(300.dp)
}

private enum class MenuOption {
    START,
    DECKS,
    STATS,
    SETTINGS
}

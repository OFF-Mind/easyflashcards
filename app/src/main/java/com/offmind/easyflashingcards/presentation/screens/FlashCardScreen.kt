package com.offmind.easyflashingcards.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.offmind.easyflashingcards.R
import com.offmind.easyflashingcards.presentation.AppBarSettings
import com.offmind.easyflashingcards.presentation.viewmodel.CardFlashViewModel
import com.offmind.easyflashingcards.ui.theme.DarkWhite
import com.offmind.easyflashingcards.ui.theme.NegativeRed
import com.offmind.easyflashingcards.ui.theme.PositiveGreen
import com.offmind.easyflashingcards.ui.theme.ShadowedDarkWhite
import org.koin.androidx.compose.koinViewModel

@Composable
fun FlashCardScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    appBarSettings: MutableState<AppBarSettings>,
    viewModel: CardFlashViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsState()
    appBarSettings.value = appBarSettings.value.copy(
        title = state.title,
        showHomeButton = true
    )

    Box(modifier = Modifier.padding(paddingValues)) {
        when (state) {
            is CardFlashViewModel.CardFlashState.ShowCardClosed -> {
                val card = (state as CardFlashViewModel.CardFlashState.ShowCardClosed).card
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp), contentAlignment = Alignment.Center
                ) {
                    CardView(title = card.frontSide, background = DarkWhite) {
                        viewModel.onOpenCard()
                    }
                }
            }

            is CardFlashViewModel.CardFlashState.ShowCardOpened -> {
                val card = (state as CardFlashViewModel.CardFlashState.ShowCardOpened).card
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    CardView(title = card.frontSide, background = DarkWhite)
                    Spacer(modifier = Modifier.height(10.dp))
                    CardView(title = card.backSide, background = ShadowedDarkWhite)
                    Spacer(modifier = Modifier.weight(1f))
                    NextButtonsBlock(
                        onNegative = {
                            viewModel.onNextCard()
                        },
                        onPositive = {
                            viewModel.onNextCard()
                        })
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }

            CardFlashViewModel.CardFlashState.Loading -> {

            }
        }
    }
}

@Composable
fun NextButtonsBlock(onNegative: () -> Unit, onPositive: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Surface(shadowElevation = 5.dp) {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable {
                        onNegative.invoke()
                    }) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.circle_button_background),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(NegativeRed)
                )
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.cancel_icon),
                    contentDescription = ""
                )
            }
        }
        Surface(shadowElevation = 5.dp) {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier.clickable {
                    onNegative.invoke()
                }) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.circle_button_background),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(PositiveGreen)
                )
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.done_icon),
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun CardView(title: String, background: Color, onCardClicked: (() -> Unit)? = null) {
    val clickModifier = if (onCardClicked != null)
        Modifier.clickable {
            onCardClicked.invoke()
        } else Modifier

    Surface(
        modifier = clickModifier,
        shadowElevation = 7.dp,
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = background)
                .height(170.dp), contentAlignment = Alignment.Center
        ) {
            Text(text = title, fontSize = 20.sp)
        }
    }
}
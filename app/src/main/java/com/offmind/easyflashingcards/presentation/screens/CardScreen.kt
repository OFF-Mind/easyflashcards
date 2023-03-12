package com.offmind.easyflashingcards.presentation.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.offmind.easyflashingcards.R
import com.offmind.easyflashingcards.data.datasource.entity.ContentType
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.presentation.ScreenSettings
import com.offmind.easyflashingcards.presentation.TopBarAction
import com.offmind.easyflashingcards.presentation.viewmodel.CardViewModel
import com.offmind.easyflashingcards.presentation.views.CardButton
import com.offmind.easyflashingcards.presentation.views.CardButtonSize
import com.offmind.easyflashingcards.presentation.views.DialogResult
import com.offmind.easyflashingcards.presentation.views.RadioGroup
import com.offmind.easyflashingcards.presentation.views.TwoButtonDialog
import com.offmind.easyflashingcards.presentation.views.VerticalDivider
import com.offmind.easyflashingcards.ui.theme.CardBackSide
import com.offmind.easyflashingcards.ui.theme.CardCornerRadiusBig
import com.offmind.easyflashingcards.ui.theme.DarkGray
import com.offmind.easyflashingcards.ui.theme.LightGray
import com.offmind.easyflashingcards.ui.theme.NegativeRed
import org.koin.androidx.compose.koinViewModel

@Composable
fun CardScreen(
    navController: NavController,
    appBarSettings: MutableState<ScreenSettings>,
    paddingValues: PaddingValues,
    viewModel: CardViewModel = koinViewModel()
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    var showDiscardDialog by remember {
        mutableStateOf(false)
    }
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    var showSaveButton by remember { mutableStateOf(false) }

    appBarSettings.value = getScreenSettings(
        state = state,
        showSave = showSaveButton,
        onDone = {
            if (viewModel.doneEditing()) {
                navController.popBackStack()
            } else {
                showDiscardDialog = true
            }
        },
        onSave = {
            showSaveButton = false
            viewModel.saveCard {
                Toast.makeText(context, "Card Saved", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        when (state) {
            is CardViewModel.CardScreenState.ShowCard -> {
                DisplayCardView(
                    card = (state as CardViewModel.CardScreenState.ShowCard).card,
                    onDelete = {
                        showDeleteDialog = true
                    }
                )
                {
                    showSaveButton = viewModel.updateCard(it)
                }
            }

            else -> {}
        }

        if (showDiscardDialog) {
            DisplayDiscardChangesDialog {
                if (it == DialogResult.POSITIVE) {
                    viewModel.saveCard {
                        navController.popBackStack()
                    }
                } else if (it == DialogResult.NEGATIVE) {
                    navController.popBackStack()
                }
                showDiscardDialog = false
            }
        }

        if (showDeleteDialog) {
            TwoButtonDialog(
                title = "Delete card?",
                label = "Are you sure you want to delete this card?",
                positiveButtonText = "Yes",
                negativeButtonText = "No"
            ) {
                if (it == DialogResult.POSITIVE) {
                    viewModel.deleteCard {
                        navController.popBackStack()
                    }
                }
                showDeleteDialog = false
            }
        }
    }
}

fun getScreenSettings(
    state: CardViewModel.CardScreenState,
    showSave: Boolean,
    onDone: () -> Unit,
    onSave: () -> Unit
): ScreenSettings {
    return ScreenSettings(
        title = state.title,
        showHomeButton = true,
        homeButtonAction = {
            onDone.invoke()
        },
        actions = if (showSave) listOf(
            TopBarAction(icon = R.drawable.v_button_icon) {
                onSave.invoke()
            }
        ) else emptyList()
    )
}


@Composable
fun DisplayDiscardChangesDialog(action: (DialogResult) -> Unit) {
    TwoButtonDialog(
        title = "Save changes?",
        label = "You have some unsaved changes, do you want to save them before exit?",
        positiveButtonText = "Yes",
        negativeButtonText = "No"
    ) {
        action(it)
    }
}

@Composable
fun DisplayCardView(
    card: Card,
    onDelete: () -> Unit,
    updateCard: (Card) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        CardContent(frontSide = card.frontSide, backSide = card.backSide)
        { front, back ->
            updateCard(card.copy(frontSide = front, backSide = back))
        }
        Spacer(modifier = Modifier.height(10.dp))
        CardTypeSelector(card)
        { contentType ->
            updateCard(card.copy(contentType = contentType))
        }
        Spacer(modifier = Modifier.height(10.dp))
        EnableCardSelector(card = card) {
            updateCard(card.copy(enabled = it))
        }
        VerticalDivider()
        ButtonsContainer(
            onChangeDeck = {},
            onShowStatistics = {},
            onDelete = { onDelete.invoke() }
        )
    }
}

@Composable
fun ButtonsContainer(
    onShowStatistics:()->Unit,
    onChangeDeck:()->Unit,
    onDelete:()->Unit
){
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        CardButton(
            size = CardButtonSize.TINY,
            icon = R.drawable.statistics_icon,
            text = "Statistics"
        ) {
            onShowStatistics.invoke()
        }
        CardButton(
            size = CardButtonSize.TINY,
            icon = R.drawable.switch_deck_icon,
            text = "Change deck"
        ) {
            onChangeDeck.invoke()
        }
        CardButton(
            size = CardButtonSize.TINY,
            icon = R.drawable.trash_icon,
            text = "Delete"
        ) {
            onDelete.invoke()
        }
    }
}

@Composable
fun EnableCardSelector(card: Card, onChanged: (Boolean) -> Unit) {
    var checkedState by remember { mutableStateOf(card.enabled) }
    Surface(
        shadowElevation = 7.dp,
        shape = RoundedCornerShape(CardCornerRadiusBig),
        modifier = Modifier.clickable {
            checkedState = checkedState.not()
            onChanged.invoke(checkedState)
        },
    ) {
        Row(
            modifier = Modifier
                .padding(
                    10.dp
                )
                .fillMaxWidth()
               ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = checkedState,
                onCheckedChange = {
                    checkedState = it
                    onChanged.invoke(checkedState)
                }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Enabled", fontSize = 17.sp)
        }
    }
}

@Composable
fun CardTypeSelector(card: Card, onTypeChanged: (ContentType) -> Unit) {
    var selectedType by remember {
        mutableStateOf(card.contentType)
    }
    Surface(
        shadowElevation = 7.dp,
        shape = RoundedCornerShape(CardCornerRadiusBig),
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Text(text = "Select card type:", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(5.dp))
            RadioGroup(
                items = ContentType.values().map { it.label },
                selected = selectedType.label,
                setSelected = { newValue ->
                    selectedType =
                        ContentType.values().find { it.label == newValue } ?: ContentType.UNDEFINED
                    onTypeChanged.invoke(selectedType)
                }
            )
        }
    }
}

@Composable
fun CardContent(
    frontSide: String,
    backSide: String,
    maxSideLength: Int = 120,
    onChanged: (String, String) -> Unit
) {

    var frontSideValue by remember {
        mutableStateOf(frontSide)
    }

    var backSideValue by remember {
        mutableStateOf(backSide)
    }

    Surface(shadowElevation = 7.dp, shape = RoundedCornerShape(CardCornerRadiusBig)) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    maxLines = 4,
                    value = frontSideValue,
                    textStyle = TextStyle(fontSize = 20.sp),
                    onValueChange = { text ->
                        frontSideValue = if (text.length > maxSideLength) {
                            text.substring(0, maxSideLength)
                        } else {
                            text
                        }
                        onChanged(frontSideValue, backSideValue)
                    })
                if (frontSideValue.isEmpty()) {
                    Text(
                        text = "Type front side here",
                        fontSize = 20.sp,
                        color = LightGray,
                        textAlign = TextAlign.Center
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(3.dp), verticalAlignment = Alignment.Bottom
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${frontSideValue.length}/${maxSideLength}",
                        color = if (backSideValue.length < maxSideLength) {
                            DarkGray
                        } else {
                            NegativeRed
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(color = CardBackSide),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    value = backSideValue,
                    maxLines = 4,
                    textStyle = TextStyle(fontSize = 20.sp),
                    onValueChange = { text ->
                        backSideValue = text
                        onChanged(frontSideValue, backSideValue)
                    })
                if (backSideValue.isEmpty()) {
                    Text(
                        text = "Type back side here",
                        fontSize = 20.sp,
                        color = LightGray,
                        textAlign = TextAlign.Center
                    )
                }
                Row(modifier = Modifier.fillMaxSize().padding(3.dp), verticalAlignment = Alignment.Bottom) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${backSideValue.length}/${maxSideLength}",
                        color = if (backSideValue.length < maxSideLength) {
                            DarkGray
                        } else {
                            NegativeRed
                        }
                    )
                }
            }
        }
    }
}
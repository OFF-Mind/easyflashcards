package com.offmind.easyflashingcards.presentation.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.offmind.easyflashingcards.R
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.ui.theme.CardCornerRadiusBig
import com.offmind.easyflashingcards.ui.theme.CardCornerRadiusMedium
import com.offmind.easyflashingcards.ui.theme.CircleButtonBackground
import com.offmind.easyflashingcards.ui.theme.DarkWhite
import com.offmind.easyflashingcards.ui.theme.DividerColor
import com.offmind.easyflashingcards.ui.theme.HighlightedColor
import com.offmind.easyflashingcards.ui.theme.LightGray

@Composable
fun CardButton(
    size: CardButtonSize,
    icon: Int,
    text: String,
    onSelect: () -> Unit
) {
    val iconSize = if (size == CardButtonSize.TINY) 40.dp else 70.dp
    Surface(shadowElevation = 7.dp, shape = RoundedCornerShape(CardCornerRadiusMedium)) {
        Column(modifier = Modifier
            .width(size.width)
            .height(size.height)
            .background(DarkWhite)
            .clickable { onSelect.invoke() }
            .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = "",
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = text)
        }
    }
}

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearchChanged: (newSearchQuery: String) -> Unit
) {
    var fieldValue by remember { mutableStateOf("") }
    Surface(
        shadowElevation = 7.dp,
        shape = RoundedCornerShape(CardCornerRadiusBig),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (fieldValue.isEmpty()) {
                    Text(
                        text = hint,
                        modifier = Modifier.fillMaxSize(),
                        fontSize = 20.sp,
                        color = LightGray
                    )
                }
                BasicTextField(
                    singleLine = true,
                    modifier = Modifier.fillMaxSize(),
                    value = fieldValue,
                    textStyle = TextStyle(fontSize = 20.sp),
                    onValueChange = { text ->
                        fieldValue = text
                        onSearchChanged.invoke(fieldValue)
                    })
            }
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.search_icon),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun VerticalDivider(
    paddingTop: Dp = 20.dp,
    paddingBottom: Dp = 20.dp
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(paddingTop))
        Divider(
            color = DividerColor,
            thickness = 0.5.dp,
            modifier = Modifier.padding(horizontal = 50.dp)
        )
        Spacer(modifier = Modifier.height(paddingBottom))
    }
}

@Composable
fun WordListItem(card: Card, highlitedText: String, onClick: (Card) -> Unit) {
    Surface(
        shadowElevation = 7.dp,
        shape = RoundedCornerShape(CardCornerRadiusBig),
        modifier = Modifier
            .padding(10.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = card.frontSide.getSpannableStringWithHighlight(highlitedText),
                        maxLines = 2,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        modifier = Modifier
                            .background(color = CircleButtonBackground, shape = CircleShape)
                            .padding(5.dp)
                            .clickable { onClick.invoke(card) }
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.card_settings_icon),
                            contentDescription = ""
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.LightGray),
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = card.backSide.getSpannableStringWithHighlight(highlitedText),
                    maxLines = 2,
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

fun String.getSpannableStringWithHighlight(
    highlightedText: String,
    spanStyle: SpanStyle = SpanStyle(background = HighlightedColor)
): AnnotatedString =
    buildAnnotatedString {
        val text = this@getSpannableStringWithHighlight
        val matchedResults = "(?i)${highlightedText}".toRegex().findAll(text)
        if (matchedResults.count() == 0) {
            append(text)
        } else {
            var startIndex = 0
            matchedResults.forEach { match ->
                append(
                    text.substring(
                        IntRange(
                            startIndex,
                            match.range.first - 1
                        )
                    )
                )
                withStyle(style = spanStyle) {
                    append(text.substring(match.range))
                }
                startIndex = match.range.last + 1
            }
            if (startIndex < text.length) {
                append(
                    text.substring(
                        IntRange(
                            startIndex,
                            text.length - 1
                        )
                    )
                )
            }
        }
    }


enum class CardButtonSize(val width: Dp, val height: Dp) {
    TINY(width = 120.dp, height = 90.dp),
    SMALL(width = 120.dp, height = 140.dp),
    MEDIUM(width = 240.dp, height = 140.dp),
    WIDE(width = 380.dp, height = 140.dp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputDialogDialog(
    title: String,
    label: String,
    positiveButtonText: String = "Ok",
    negativeButtonText: String = "Cancel",
    onResult: (result: DialogResult, insertedText: String) -> Unit
) {
    var dialogName by remember {
        mutableStateOf("")
    }
    AlertDialog(
        onDismissRequest = {
            onResult.invoke(DialogResult.NEUTRAL, "")
        },
        title = {
            Text(text = title)
        },
        text = {
            Column {
                TextField(
                    value = dialogName,
                    textStyle = TextStyle(fontSize = 20.sp),
                    label = { Text(text = label) },
                    onValueChange = {
                        dialogName = it
                    })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onResult.invoke(DialogResult.POSITIVE, dialogName)
                }) {
                Text(positiveButtonText)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onResult.invoke(DialogResult.NEGATIVE, "")
                }) {
                Text(negativeButtonText)
            }
        }
    )
}

@Composable
fun RadioGroup(
    items: List<String>,
    selected: String,
    setSelected: (selected: String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == item,
                    onClick = {
                        setSelected(item)
                    },
                    enabled = true
                )
                Text(text = item, modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        setSelected(item)
                    })
            }
        }
    }
}

@Composable
fun TwoButtonDialog(
    title: String,
    label: String,
    positiveButtonText: String = "Ok",
    negativeButtonText: String = "Cancel",
    onResult: (result: DialogResult) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onResult.invoke(DialogResult.NEUTRAL)
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = label)
        },
        confirmButton = {
            Button(
                onClick = {
                    onResult.invoke(DialogResult.POSITIVE)
                }) {
                Text(positiveButtonText)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onResult.invoke(DialogResult.NEGATIVE)
                }) {
                Text(negativeButtonText)
            }
        }
    )
}

enum class DialogResult {
    POSITIVE,
    NEGATIVE,
    NEUTRAL
}



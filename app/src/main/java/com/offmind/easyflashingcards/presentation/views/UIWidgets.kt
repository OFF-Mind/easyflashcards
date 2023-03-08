package com.offmind.easyflashingcards.presentation.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.offmind.easyflashingcards.R
import com.offmind.easyflashingcards.ui.theme.DarkWhite

@Composable
fun CardButton(
    size: CardButtonSize,
    icon: Int,
    text: String,
    onSelect: () -> Unit
) {
    val iconSize = if (size == CardButtonSize.TINY) 40.dp else 70.dp
    Surface(shadowElevation = 7.dp, shape = RoundedCornerShape(4.dp)) {
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
    onSearchChanged: (newSearchQuery: String) -> Unit
) {
    var fieldValue by remember { mutableStateOf("") }
    Surface(
        shadowElevation = 7.dp,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                singleLine = true,
                modifier = Modifier.weight(1f),
                value = fieldValue,
                textStyle = TextStyle(fontSize = 20.sp),
                onValueChange = { text ->
                    fieldValue = text
                    onSearchChanged.invoke(fieldValue)
                })

            Spacer(modifier = Modifier.width(10.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.search_icon),
                contentDescription = ""
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


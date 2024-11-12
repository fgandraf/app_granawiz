package view.shared

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.theme.Ubuntu

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    boxSize: Dp = 35.dp,
    textAlign: TextAlign = TextAlign.Start,
    value: String,
    label: String? = null,
    placeholder: String = "",
    onValueChange: (String) -> Unit
){
    val primaryColor = MaterialTheme.colors.primary
    val secondaryColor = MaterialTheme.colors.secondary

    var borderSize by remember { mutableStateOf(1.dp) }
    var borderColor by remember { mutableStateOf(primaryColor) }

    Column(modifier = modifier) {
        if (label != null)
            TextPrimary(text = label, modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)

        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(boxSize)
                .border(borderSize, borderColor, shape = RoundedCornerShape(5.dp))
                .clip(RoundedCornerShape(5.dp))
                .padding(10.dp)
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused){ borderSize = 1.2.dp; borderColor = secondaryColor }
                        else { borderSize = 1.dp; borderColor = primaryColor }
                    },
                singleLine = boxSize <= 35.dp,
                textStyle = TextStyle(
                    fontFamily = Ubuntu,
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primary,
                    lineHeight = 16.sp,
                    textAlign = textAlign
                ),
                value = value,
                onValueChange = onValueChange,
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (value.isEmpty())
                            TextPrimary(
                                text = placeholder,
                                size = 10.sp,
                                align = textAlign,
                                color = primaryColor.copy(alpha = 0.75f),
                                modifier = Modifier.fillMaxWidth())
                    }
                    innerTextField()
                }
            )
        }
    }
}
package view.shared

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.theme.Ubuntu

@Composable
fun DefaultTextField(
    value: String,
    onValueChange: (String) -> Unit
){
    val primaryColor = MaterialTheme.colors.primary
    val secondaryColor = MaterialTheme.colors.secondary

    var borderSize by remember { mutableStateOf(1.dp) }
    var borderColor by remember { mutableStateOf(primaryColor) }

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .border(borderSize, borderColor, shape = RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
            .padding(10.dp)
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth().onFocusChanged { focusState ->
                if (focusState.isFocused){ borderSize = 1.2.dp; borderColor = secondaryColor }
                else { borderSize = 1.dp; borderColor = primaryColor }
            },
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = Ubuntu,
                fontSize = 12.sp,
                color = MaterialTheme.colors.primary,
                lineHeight = 16.sp
            ),
            value = value,
            onValueChange = onValueChange
        )
    }
}
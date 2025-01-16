package view.shared

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

@Composable
fun FocusableBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    val primaryColor = MaterialTheme.colors.primary
    val secondaryColor = MaterialTheme.colors.secondary

    var borderSize by remember { mutableStateOf(1.dp) }
    var borderColor by remember { mutableStateOf(primaryColor) }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth()
            .height(35.dp)
            .border(borderSize, borderColor, shape = RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
            .pointerHoverIcon(PointerIcon.Hand)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    borderSize = 1.2.dp; borderColor = secondaryColor
                } else {
                    borderSize = 1.dp; borderColor = primaryColor
                }
            }
            .clickable { onClick() }
            .padding(horizontal = 10.dp)
    ) {
        content()
    }

}
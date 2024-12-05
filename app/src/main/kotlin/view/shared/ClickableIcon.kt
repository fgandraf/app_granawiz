package view.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    shape: Shape = RoundedCornerShape(6.dp),
    color: Color = MaterialTheme.colors.primary,
    iconSize: Dp = 18.dp,
    padding: Boolean = false,
    onClick: () -> Unit
    ) {

    Box(Modifier
        .size(if (padding) iconSize * 1.25f else iconSize)
        .clip(shape)
        .pointerHoverIcon(PointerIcon.Hand)
        .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = modifier.size(iconSize).align(Alignment.Center)
        )
    }
}

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
    shape: Shape? = null,
    color: Color = MaterialTheme.colors.primary,
    boxSize: Dp = 30.dp,
    iconSize: Dp = 15.dp,
    enabled: Boolean? = true,
    onClick: () -> Unit,
) {

    val stateModifier = if (enabled!!)
        modifier.clip(shape ?: RoundedCornerShape(6.dp)).clickable(onClick = onClick).pointerHoverIcon(PointerIcon.Hand)
    else
        modifier.clip(shape ?: RoundedCornerShape(6.dp))


    Box(
        stateModifier
            .size(boxSize)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) color else MaterialTheme.colors.primaryVariant,
            modifier = modifier.size(iconSize).align(Alignment.Center)
        )
    }
}

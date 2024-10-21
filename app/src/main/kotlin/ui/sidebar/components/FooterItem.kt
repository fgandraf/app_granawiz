package ui.sidebar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun FooterItem(
    iconResource: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val inactiveColor = MaterialTheme.colors.primary
    val activeColor = MaterialTheme.colors.onPrimary

    val iconColor = if (isSelected) activeColor else inactiveColor
    val barColor = if (isSelected) activeColor else Color.Transparent

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconResource),
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(25.dp)
        )
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(barColor)
                .align(Alignment.BottomCenter)
        )
    }
}
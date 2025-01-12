package view.modules.sidebar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import view.modules.Screen
import view.shared.TextH3

@Composable
fun StaticMenuItem(
    icon: ImageVector,
    isActive: Boolean,
    label: String,
    screen: Screen,
    onClick: (Screen) -> Unit
){
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(35.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isActive) MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f) else Color.Transparent )
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { onClick(screen) }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(20.dp).offset(x = 10.dp),
        )
        TextH3(
            modifier = Modifier.offset(x = 22.dp),
            text = label
        )
    }
}
package view.modules.sidebar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.modules.Screen
import view.theme.Afacade

@Composable
fun StaticMenuItem(
    icon: ImageVector,
    label: String,
    screen: Screen,
    currentScreen: Screen,
    onClick: (Screen) -> Unit
){
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(35.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (currentScreen == screen) MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f) else Color.Transparent )
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { onClick(screen) }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(20.dp).offset(x = 10.dp),
        )
        Text(modifier = Modifier.offset(x = 22.dp),
            text = label,
            fontSize  = 14.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Medium,
            lineHeight = 0.sp,
            fontFamily = Afacade
        )
    }
}
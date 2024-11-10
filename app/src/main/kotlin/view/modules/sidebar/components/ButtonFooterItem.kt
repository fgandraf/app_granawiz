package view.modules.sidebar.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.IconPaths
import view.theme.Afacade

@Composable
fun ButtonFooterItem(
    modifier: Modifier = Modifier,
    iconResource: String,
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {

    val color = if (enabled) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
    val newModifier = if (enabled) modifier.clickable(onClick = onClick).pointerHoverIcon(PointerIcon.Hand) else modifier

    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = newModifier.fillMaxHeight()
    ) {
        Icon(
            painter = painterResource(IconPaths.SYSTEM_ICONS + iconResource),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.height(1.dp))

        Text(
            text = label,
            fontSize  = 12.sp,
            color =  color,
            fontWeight = FontWeight.Normal,
            lineHeight = 0.sp,
            fontFamily = Afacade
        )
    }

}
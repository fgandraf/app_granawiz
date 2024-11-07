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
    onClick: () -> Unit
) {

    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxHeight()
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { onClick() }
    ) {
        Icon(
            painter = painterResource(IconPaths.SYSTEM_ICONS + iconResource),
            contentDescription = null,
            tint = MaterialTheme.colors.secondary,
            modifier = Modifier.size(30.dp, 20.dp).padding(end = 5.dp)
        )
        Spacer(Modifier.height(2.dp))

        Text(
            text = label,
            fontSize  = 12.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Normal,
            lineHeight = 0.sp,
            fontFamily = Afacade
        )
    }

}
package view.modules.addAccount.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import config.IconPaths

@Composable
fun IconSelector(
    icon: String,
    onIconSelected: (String) -> Unit,
){

    var expandedIcons by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(Color.White, CircleShape)
            .clip(CircleShape)
            .border(0.8.dp, MaterialTheme.colors.primaryVariant, CircleShape)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { expandedIcons = !expandedIcons }
    ) {
        Box(
            modifier = Modifier.padding(15.dp)
        ) {
            Image(
                modifier = Modifier.size(60.dp),
                painter = painterResource(IconPaths.BANK_LOGOS + icon),
                contentDescription = "",
                alpha = 0.5f
            )
            Image(
                modifier = Modifier.size(14.dp).align(Alignment.BottomEnd),
                painter = painterResource(IconPaths.SYSTEM_ICONS + "edit.svg"),
                contentDescription = "",
                alpha = 0.8f
            )
        }
        DropDownIcons(
            expanded = expandedIcons,
            onDismissRequest = { expandedIcons = false },
            onIconSelected = onIconSelected
        )
    }
}
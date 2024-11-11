package view.shared

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
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
fun ComboBoxField(
    label: String,
    focused: Boolean = true,
    expanded: Boolean = false,
    onClick: () -> Unit = {}
){

    var borderSize by remember { mutableStateOf(0.dp) }
    var borderColor by remember { mutableStateOf(Color.Transparent) }

    if (focused) {
        borderSize = 1.2.dp
        borderColor = MaterialTheme.colors.secondary
    }else{
        borderSize = 1.dp
        borderColor = MaterialTheme.colors.primary
    }


    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .border(borderSize, borderColor, shape = RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable{ onClick() }
    ) {

        TextPrimary(text = label, modifier = Modifier.padding(start = 10.dp))

        Box(Modifier.size(35.dp)
        ){
            Icon(
                painter = painterResource(if (expanded) IconPaths.SYSTEM_ICONS + "toggle_down.svg" else IconPaths.SYSTEM_ICONS + "toggle_right.svg"),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(15.dp).align(Alignment.Center)
            )
        }

    }
}
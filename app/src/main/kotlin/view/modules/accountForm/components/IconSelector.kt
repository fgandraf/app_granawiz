package view.modules.accountForm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.fill.PencilLine
import utils.IconPaths

@Composable
fun IconSelector(
    icon: String,
    onIconSelected: (String) -> Unit,
) {

    var expandedIcons by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colors.surface, CircleShape)
            .clip(CircleShape)
            .border(0.8.dp, MaterialTheme.colors.primaryVariant, CircleShape)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { expandedIcons = !expandedIcons }
    ) {
        Box(
            modifier = Modifier.padding(15.dp)
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(IconPaths.BANK_LOGOS + icon),
                contentDescription = null,
                tint = MaterialTheme.colors.primaryVariant
            )
            Icon(
                modifier = Modifier.size(14.dp).align(Alignment.BottomEnd),
                imageVector = PhosphorIcons.Fill.PencilLine,
                contentDescription = "",
                tint = MaterialTheme.colors.primary
            )
        }
        DropDownIcons(
            expanded = expandedIcons,
            onDismissRequest = { expandedIcons = false },
            onIconSelected = onIconSelected
        )
    }
}
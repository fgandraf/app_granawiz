package view.modules.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CalendarBlank
import com.adamglin.phosphoricons.light.CaretDown
import core.structs.DashboardPeriod
import view.shared.TextNormal

@Composable
fun PeriodSelector(
    current: DashboardPeriod,
    onSelect: (DashboardPeriod) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Box(
            modifier = Modifier
                .height(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(8.dp))
                .background(Color.Transparent)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { expanded = true }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = PhosphorIcons.Light.CalendarBlank,
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(16.dp),
                )
                TextNormal(text = current.label)
                Icon(
                    imageVector = PhosphorIcons.Light.CaretDown,
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DashboardPeriod.all.forEach { p ->
                DropdownMenuItem(onClick = {
                    onSelect(p)
                    expanded = false
                }) {
                    TextNormal(text = p.label)
                }
            }
        }
    }
}

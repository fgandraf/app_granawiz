package view.modules.schedules.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
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
import com.adamglin.phosphoricons.light.Plus
import view.theme.ButtonPurple

@Composable
fun AddScheduleButton(
    onClickGain: () -> Unit,
    onClickExpense: () -> Unit,
) {
    var showDropdown by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize().padding(bottom = 50.dp, end = 5.dp)) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(ButtonPurple)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showDropdown = true }
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                modifier = Modifier.size(25.dp).align(Alignment.Center),
                imageVector = PhosphorIcons.Light.Plus,
                contentDescription = "Add schedule",
                tint = Color.White
            )
            if (showDropdown) {
                DropDownAddSchedule(
                    expanded = showDropdown,
                    onClickGain = { showDropdown = false; onClickGain() },
                    onClickExpense = { showDropdown = false; onClickExpense() },
                    onDismissRequest = { showDropdown = false }
                )
            }
        }
    }
}

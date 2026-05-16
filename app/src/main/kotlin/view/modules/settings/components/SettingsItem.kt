package view.modules.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CaretDown
import com.adamglin.phosphoricons.light.CaretUp
import view.shared.TextH4
import view.shared.TextNormal

@Composable
fun SettingsItem(
    label: String,
    value: String,
    dropdownContent: @Composable (onDismiss: () -> Unit) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Row(Modifier.weight(0.5f)) { TextH4(text = "$label:") }

        BoxWithConstraints(Modifier.weight(1f)) {
            var focused by remember { mutableStateOf(false) }

            Column {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                        .pointerHoverIcon(PointerIcon.Hand)
                        .onFocusChanged { focusState -> focused = focusState.isFocused }
                        .clickable { expanded = true }
                        .padding(horizontal = 10.dp)
                ) {
                    if (value.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextNormal(text = value, modifier = Modifier.padding(start = 5.dp))
                        }
                    }

                    Icon(
                        imageVector = if (expanded) PhosphorIcons.Light.CaretUp else PhosphorIcons.Light.CaretDown,
                        contentDescription = "click",
                        modifier = Modifier.size(15.dp).align(Alignment.CenterEnd),
                        tint = MaterialTheme.colors.primary
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.wrapContentHeight().width(maxWidth)
            ) {
                dropdownContent { expanded = false }
            }
        }
    }
}

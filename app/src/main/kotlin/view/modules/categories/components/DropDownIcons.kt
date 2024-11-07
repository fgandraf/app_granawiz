package view.modules.categories.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import utils.rememberSvgPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import view.shared.FlowLayout
import utils.IconPaths

@Composable
fun DropDownIcons(
    width: Dp = 350.dp,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onIconSelected: (String) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        DropdownMenu(
            modifier = Modifier.weight(1f).padding(horizontal = 10.dp).width(width),
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {
            FlowLayout(maxWidth = width) {
                IconPaths.categoryIcons.forEach { fileName ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(35.dp)
                    ) {
                        Image(
                            painter = rememberSvgPainter(IconPaths.CATEGORY_PACK + fileName),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.primary),
                            modifier = Modifier
                                .pointerHoverIcon(PointerIcon.Hand)
                                .clickable {
                                    onIconSelected(fileName)
                                    onDismissRequest()
                                }
                                .padding(5.dp)
                                .size(25.dp)
                        )
                    }
                }
            }
        }
    }
}
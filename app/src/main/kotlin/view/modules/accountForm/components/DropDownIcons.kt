package view.modules.accountForm.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import utils.rememberSvgPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.fill.FolderOpen
import view.shared.FlowLayout
import view.shared.TextSmall
import utils.IconPaths

@Composable
fun DropDownIcons(
    width: Dp = 320.dp,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onIconSelected: (String) -> Unit,
    onCustomIconRequest: () -> Unit = {},
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        DropdownMenu(
            modifier = Modifier.weight(1f).padding(horizontal = 10.dp).width(width),
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {
            FlowLayout(maxWidth = width) {
                IconPaths.bankLogos.forEach { fileName ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(80.dp)
                    ) {
                        Image(
                            painter = rememberSvgPainter(IconPaths.BANK_LOGOS + fileName),
                            contentDescription = "",
                            modifier = Modifier
                                .pointerHoverIcon(PointerIcon.Hand)
                                .clickable {
                                    onIconSelected(fileName)
                                    onDismissRequest()
                                }
                                .padding(5.dp)
                                .size(40.dp)
                        )
                        TextSmall(text = fileName.removeSuffix(".svg"))
                    }
                }
            }

            Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable {
                        onDismissRequest()
                        onCustomIconRequest()
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = PhosphorIcons.Fill.FolderOpen,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colors.primary
                )
                Spacer(Modifier.width(8.dp))
                TextSmall(text = "Ícone personalizado (SVG)…")
            }
        }
    }
}

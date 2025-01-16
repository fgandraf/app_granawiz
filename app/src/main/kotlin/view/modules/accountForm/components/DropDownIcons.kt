package view.modules.accountForm.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import view.shared.FlowLayout
import view.shared.TextSmall
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

@Composable
fun DropDownIcons(
    width: Dp = 320.dp,
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
            val iconsDirectory = File("src/main/resources/assets/icons/bankLogos")
            FlowLayout(maxWidth = width) {
                iconsDirectory.listFiles()?.forEach { file ->

                    if (!file.name.startsWith("_")) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(80.dp)
                        ) {
                            Image(
                                painter = painterResource("assets/icons/bankLogos/${file.name}"),
                                contentDescription = "",
                                modifier = Modifier
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .clickable {
                                        onIconSelected(file.name)
                                        onDismissRequest()
                                    }
                                    .padding(5.dp)
                                    .size(40.dp)
                            )
                            val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                            val title = document.getElementsByTagName("title").item(0)
                            TextSmall(text = title.textContent)
                        }
                    }
                }

            }
        }
    }
}
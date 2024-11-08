package view.modules.sidebar.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import config.IconPaths
import view.shared.ClickableIcon
import view.shared.DefaultTextField
import view.shared.TextPrimary
import view.theme.Purple600
import view.theme.Ubuntu
import viewModel.SidebarViewModel

@Composable
fun DialogNewGroup(
    viewModel: SidebarViewModel,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(400.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
        ) {

            //===== Title
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(5.dp).padding(start = 5.dp)
            ) {
                TextPrimary(
                    text = "Adicionar novo grupo",
                    size = 12.sp,
                    color = MaterialTheme.colors.secondary,
                )
                ClickableIcon(
                    icon = "close",
                    padding = true,
                    color = MaterialTheme.colors.secondary,
                    onClick = onDismiss
                )
            }
            Divider()

            //===== Main
            var value by remember { mutableStateOf("") }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(40.dp)
            ) {
                Image(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(IconPaths.SYSTEM_ICONS + "group.svg"),
                    contentDescription = "Group logo"
                )
                TextPrimary(
                    modifier = Modifier.padding(top = 20.dp, bottom = 5.dp),
                    text = "Nome do novo grupo:",
                    size = 12.sp,
                    align = TextAlign.Start
                )
                DefaultTextField(value = value) { value = it }
            }
            Divider()


            val confirmed by remember { derivedStateOf { value != "" } }
            //===== Footer
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 10.dp)
            ) {

                Button(
                    modifier = Modifier.fillMaxWidth().pointerHoverIcon(if(confirmed) PointerIcon.Hand else PointerIcon.Default),
                    enabled = confirmed,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Purple600),
                    onClick = {
                        viewModel.addNewGroup(value)
                        onDismiss()
                    }
                ){
                    Text(
                        text = "Adicionar",
                        color = Color.White,
                        fontStyle = if(!confirmed) FontStyle.Italic else FontStyle.Normal,
                        fontFamily = Ubuntu,
                        fontSize = 12.sp,
                    )
                }

            }

        }
    }
}
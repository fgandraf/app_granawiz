package view.modules.groupForm

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import config.IconPaths
import core.entity.Group
import view.shared.DefaultButton
import view.shared.DefaultTextField
import view.shared.DialogTitleBar
import view.shared.TextPrimary
import viewModel.SidebarViewModel

@Composable
fun GroupForm(
    viewModel: SidebarViewModel,
    group: Group? = null,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(400.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
        ) {

            val title by remember { mutableStateOf(if (group == null) "Adicionar novo grupo" else "Editar grupo") }
            val buttonLabel by remember { mutableStateOf(if (group == null) "Adicionar" else "Editar") }
            var value by remember { mutableStateOf(group?.name ?: "") }

            DialogTitleBar(title, onDismiss)
            Divider()

            //===== Main
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
                    text = "Nome do grupo:",
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

                DefaultButton(confirmed, buttonLabel){
                    if (group == null) viewModel.addNewGroup(value) else viewModel.renameGroup(group, value)
                    onDismiss()
                }

            }

        }
    }
}
package view.modules.groupForm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Folders
import core.entity.Group
import view.shared.DefaultButton
import view.shared.DefaultTextField
import view.shared.DialogTitleBar
import view.shared.TextNormal
import viewModel.SidebarViewModel

@Composable
fun GroupForm(
    viewModel: SidebarViewModel,
    group: Group? = null,
    onDismiss: () -> Unit,
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

            DialogTitleBar(title = title, onCloseRequest = onDismiss)
            Divider(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)
                    .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.2f))
            )

            //===== Main
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(40.dp)
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = PhosphorIcons.Light.Folders,
                    contentDescription = "Group logo",
                    tint = MaterialTheme.colors.primary
                )
                TextNormal(
                    modifier = Modifier.padding(top = 20.dp, bottom = 5.dp),
                    text = "Nome do grupo:"
                )
                DefaultTextField(value = value) { value = it }
            }
            Divider(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)
                    .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.2f))
            )


            val confirmed by remember { derivedStateOf { value != "" } }
            //===== Footer
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 10.dp)
            ) {

                DefaultButton(
                    modifier = Modifier.fillMaxWidth(),
                    confirmed = confirmed,
                    text = buttonLabel,
                    textColor = Color.White
                ) {
                    if (group == null) viewModel.addNewGroup(value)
                    else viewModel.renameGroup(group, value)
                    onDismiss()
                }

            }

        }
    }
}
package view.modules.sidebar.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.entity.Group
import core.utils.brMoney
import view.modules.groupForm.GroupForm
import view.shared.ClickableIcon
import view.shared.ClickableRow
import view.theme.Afacade
import view.theme.Lime400
import view.theme.Red400
import view.theme.Ubuntu
import viewModel.SidebarViewModel

@Composable
fun GroupMenuItem(
    viewModel: SidebarViewModel,
    group: Group,
    isExpanded: Boolean,
    toggleClick: () -> Unit
) {

    Row(horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(horizontal = 10.dp)

    ) {

        var groupName by remember { mutableStateOf(group.name) }
        LaunchedEffect(group.name) { groupName = group.name }

        Row(modifier = Modifier.padding(start = 5.dp)){
            ClickableIcon(
                modifier = Modifier.size(16.dp),
                icon = if (isExpanded) "toggle_down" else "toggle_right",
                shape = RoundedCornerShape(6.dp),
                iconSize = 12.dp,
                padding = true,
                onClick = { toggleClick() },
            )

            Column(modifier = Modifier.padding(start = 7.dp)) {
                Text(
                    text = groupName,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 0.sp,
                    fontFamily = Afacade
                )

                val totalGroup = viewModel.fetchTotalGroup(group)
                Text(
                    text = brMoney.format(totalGroup),
                    fontSize = 10.sp,
                    color = if (totalGroup > 0f) Lime400 else if (totalGroup < 0f) Red400 else MaterialTheme.colors.primaryVariant,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 0.sp,
                    fontFamily = Ubuntu
                )
            }
        }

        var dropDownMenuExpanded by remember { mutableStateOf(false) }
        Row {
            ClickableIcon(
                icon = "dots",
                shape = RoundedCornerShape(6.dp),
                iconSize = 16.dp,
                padding = true,
                onClick = { dropDownMenuExpanded = !dropDownMenuExpanded }
            )

            DropDownGroupMenu(
                viewModel = viewModel,
                expanded = dropDownMenuExpanded,
                onDismissRequest = { dropDownMenuExpanded = false },
                group = group
            )
        }
    }
}

@Composable
fun DropDownGroupMenu(
    viewModel: SidebarViewModel,
    group: Group,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
) {

    Row(verticalAlignment = Alignment.CenterVertically){
        DropdownMenu(
            modifier = Modifier.weight(1f),
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {

            ClickableRow(iconResource = "move_up", label = "Mover para cima") {
                viewModel.moveGroup(group, -1)
                onDismissRequest() }

            ClickableRow(iconResource = "move_down", label = "Mover para baixo") {
                viewModel.moveGroup(group, 1)
                onDismissRequest() }

            Divider(modifier = Modifier.padding(vertical = 3.dp))

            var showNewGroupDialog by remember { mutableStateOf(false) }
            ClickableRow(iconResource = "edit", label = "Editar") { showNewGroupDialog = true }
            if (showNewGroupDialog) GroupForm(viewModel = viewModel, group = group, onDismiss = { showNewGroupDialog = false; onDismissRequest() })

            Divider(modifier = Modifier.padding(vertical = 3.dp))

            ClickableRow(iconResource = "trash", label = "Excluir", enabled = group.accounts.isEmpty()) { viewModel.deleteGroup(group) }

        }
    }

}
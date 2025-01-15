package view.modules.sidebar.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.*
import core.entity.Group
import utils.brMoney
import view.modules.groupForm.GroupForm
import view.shared.ClickableIcon
import view.shared.ClickableRow
import view.shared.TextH3
import view.shared.TextSmall
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
            .padding(start = 5.dp, end = 10.dp)

    ) {

        var groupName by remember { mutableStateOf(group.name) }
        LaunchedEffect(group.name) { groupName = group.name }

        Row(modifier = Modifier.padding(start = 5.dp)){
            ClickableIcon(
                modifier = Modifier.size(16.dp),
                icon = if (isExpanded) PhosphorIcons.Light.CaretDown else PhosphorIcons.Light.CaretRight,
                shape = RoundedCornerShape(6.dp),
                onClick = { toggleClick() },
            )

            Column(modifier = Modifier.padding(start = 7.dp)) {
                TextH3(
                    text = groupName,
                    color = MaterialTheme.colors.primary,
                )

                val totalGroup = viewModel.fetchGroupBalance(group)
                TextSmall(
                    text = brMoney.format(totalGroup),
                    color = if (totalGroup > 0f) MaterialTheme.colors.onPrimary else if (totalGroup < 0f) MaterialTheme.colors.onError else MaterialTheme.colors.primaryVariant
                )
            }
        }

        var dropDownMenuExpanded by remember { mutableStateOf(false) }
        Row {
            ClickableIcon(
                icon = PhosphorIcons.Light.DotsThree,
                shape = RoundedCornerShape(6.dp),
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

            ClickableRow(icon = PhosphorIcons.Light.ArrowLineUp, label = "Mover para cima") {
                viewModel.moveGroupPosition(group, -1)
                onDismissRequest() }

            ClickableRow(icon = PhosphorIcons.Light.ArrowLineDown, label = "Mover para baixo") {
                viewModel.moveGroupPosition(group, 1)
                onDismissRequest() }

            Divider(modifier = Modifier.padding(vertical = 3.dp))

            var showNewGroupDialog by remember { mutableStateOf(false) }
            ClickableRow(icon = PhosphorIcons.Light.PencilLine, label = "Editar") { showNewGroupDialog = true }
            if (showNewGroupDialog) GroupForm(viewModel = viewModel, group = group, onDismiss = { showNewGroupDialog = false; onDismissRequest() })

            Divider(modifier = Modifier.padding(vertical = 3.dp))

            ClickableRow(icon = PhosphorIcons.Light.Trash, label = "Excluir", enabled = group.accounts.isEmpty()) { viewModel.deleteGroup(group) }

        }
    }

}
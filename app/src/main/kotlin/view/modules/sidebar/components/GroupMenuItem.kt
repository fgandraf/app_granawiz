package view.modules.sidebar.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.entity.Group
import view.shared.ClickableIcon
import view.shared.ClickableRow
import model.utils.brMoney
import viewModel.SidebarViewModel
import view.theme.*

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
        val valueChanged = groupName != group.name

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
                BasicTextField(
                    value = groupName,
                    onValueChange = { groupName = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 0.sp,
                        fontFamily = Afacade,
                        color = if (valueChanged) Color.Blue else MaterialTheme.colors.primary
                    )
                )


                val totalGroup = viewModel.fetchTotalGroup(group)
                Text(
                    text = brMoney.format(totalGroup),
                    fontSize = 10.sp,
                    color = if (totalGroup > 0f) Lime700 else if (totalGroup < 0f) Red400 else MaterialTheme.colors.primaryVariant,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 0.sp,
                    fontFamily = Ubuntu
                )
            }
        }

        if (!valueChanged) {
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
        else
            Row {
                ClickableIcon(
                    icon = "check",
                    color = Color.Blue,
                    shape = RoundedCornerShape(6.dp),
                    iconSize = 16.dp,
                    padding = true,
                    onClick = {
                        viewModel.renameGroup(group, groupName)
                    }
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
                onDismissRequest()
            }
            ClickableRow(iconResource = "move_down", label = "Mover para baixo") {
                viewModel.moveGroup(group, 1)
                onDismissRequest()
            }



            Divider(modifier = Modifier.padding(vertical = 3.dp))
            if (group.accounts.isNotEmpty())
                ClickableRow(iconResource = "trash", label = "Excluir", enabled = false){}
            else
                ClickableRow(iconResource = "trash", label = "Excluir") { viewModel.deleteGroup(group) }

        }
    }

}
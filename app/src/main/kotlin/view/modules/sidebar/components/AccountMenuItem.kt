package view.modules.sidebar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.IconPaths
import model.entity.Group
import model.entity.account.BankAccount
import model.utils.brMoney
import view.modules.Screen
import view.modules.addAccount.AddAccount
import view.shared.ClickableIcon
import view.shared.ClickableRow
import view.shared.DialogDelete
import view.theme.Afacade
import view.theme.Lime700
import view.theme.Red400
import view.theme.Ubuntu
import viewModel.SidebarViewModel

@Composable
fun AccountMenuItem(
    viewModel: SidebarViewModel,
    account: BankAccount,
    group: Group,
    screen: Screen,
    currentScreen: Screen,
    onClick: (Screen) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .height(40.dp)
            .background(if (currentScreen == screen) Color.LightGray else Color.Transparent )
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { onClick(screen) }
    ) {

        Icon(
            painter = painterResource(IconPaths.BANK_LOGOS + account.icon),
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(25.dp).offset(x = 25.dp)
        )


        Column(modifier = Modifier.padding(start = 40.dp).weight(1f)) {
            Text(
                text = account.name,
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Medium,
                lineHeight = 0.sp,
                fontFamily = Afacade
            )
            Text(
                text = brMoney.format(account.balance),
                fontSize = 10.sp,
                color = if(account.balance > 0f) Lime700 else if (account.balance < 0f) Red400 else MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Normal,
                lineHeight = 0.sp,
                fontFamily = Ubuntu
            )
        }

        ClickableIcon(
            icon = "dots",
            shape = RoundedCornerShape(6.dp),
            iconSize = 16.dp,
            padding = true,
            onClick = {expanded = !expanded}
        )

        DropDownAccountMenu(
            viewModel = viewModel,
            group = group,
            expanded = expanded,
            onDismissRequest = { expanded = false },
            account = account
        )
    }

    Spacer(Modifier.height(5.dp))
}

@Composable
fun DropDownAccountMenu(
    viewModel: SidebarViewModel,
    group: Group,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    account: BankAccount
) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        DropdownMenu(
            modifier = Modifier.weight(1f).padding(horizontal = 10.dp),
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {

            ClickableRow(iconResource = "move_up", label = "Mover para cima") {
                viewModel.moveAccount(group, account,-1)
                onDismissRequest()
            }

            ClickableRow(iconResource = "move_down", label = "Mover para baixo") {
                viewModel.moveAccount(group, account,1)
                onDismissRequest()
            }

            Divider(modifier = Modifier.padding(vertical = 3.dp))

            var showEditAccount by remember { mutableStateOf(false) }
            ClickableRow(iconResource = "edit", label = "Editar") { showEditAccount = true }
            if (showEditAccount) AddAccount(sidebarViewModel = viewModel, account = account, onDismiss = { showEditAccount = false; onDismissRequest() })


            Divider(modifier = Modifier.padding(vertical = 3.dp))


            var deleteDialog by remember { mutableStateOf(false) }
            ClickableRow(iconResource = "trash", label = "Excluir") { deleteDialog = true }
            if (deleteDialog)
                DialogDelete(
                    title = "Excluir conta",
                    iconResource = IconPaths.BANK_LOGOS + account.icon,
                    objectName = "${account.group.name}/${account.name}",
                    alertText = "Isso irá excluir permanentemente a conta ${account.group.name} → ${account.name}, bem como todas as transações associadas a ela.",
                    onClickButton = { viewModel.deleteAccount(account); onDismissRequest() },
                    onDismiss = { onDismissRequest(); deleteDialog = false }
                )
        }
    }

}
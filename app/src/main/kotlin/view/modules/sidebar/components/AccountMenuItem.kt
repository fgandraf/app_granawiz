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
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.*
import core.entity.account.BankAccount
import utils.IconPaths
import utils.brMoney
import view.modules.Screen
import view.modules.accountForm.AccountForm
import view.shared.ClickableIcon
import view.shared.ClickableRow
import view.theme.*
import viewModel.SidebarViewModel

@Composable
fun AccountMenuItem(
    viewModel: SidebarViewModel,
    account: BankAccount,
    screen: Screen,
    isActive: Boolean,
    onClick: (Screen) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .height(40.dp)
            .background(if (isActive) MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f) else Color.Transparent )
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
                color = if(account.balance > 0f) Lime200 else if (account.balance < 0f) Red400 else MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Normal,
                lineHeight = 0.sp,
                fontFamily = Ubuntu
            )
        }

        ClickableIcon(
            icon = PhosphorIcons.Light.DotsThree,
            shape = RoundedCornerShape(6.dp),
            onClick = {expanded = !expanded}
        )

        DropDownAccountMenu(
            viewModel = viewModel,
            expanded = expanded,
            onDismissRequest = { expanded = false; },
            account = account
        )
    }

    Spacer(Modifier.height(5.dp))
}

@Composable
fun DropDownAccountMenu(
    viewModel: SidebarViewModel,
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

            ClickableRow(icon = PhosphorIcons.Light.ArrowLineUp, label = "Mover para cima") {
                viewModel.groupService.moveAccountPosition(account,-1)
                onDismissRequest()
            }

            ClickableRow(icon = PhosphorIcons.Light.ArrowLineDown, label = "Mover para baixo") {
                viewModel.groupService.moveAccountPosition(account,1)
                onDismissRequest()
            }

            Divider(modifier = Modifier.padding(vertical = 3.dp))

            var showEditAccount by remember { mutableStateOf(false) }
            ClickableRow(icon = PhosphorIcons.Light.PencilLine, label = "Editar") { showEditAccount = true; }
            if (showEditAccount) AccountForm(sidebarViewModel = viewModel, account = account, onDismiss = { showEditAccount = false; onDismissRequest() })


            Divider(modifier = Modifier.padding(vertical = 3.dp))


            var deleteDialog by remember { mutableStateOf(false) }
            ClickableRow(icon = PhosphorIcons.Light.Trash, label = "Excluir") { deleteDialog = true }
            if (deleteDialog)
                DialogDeleteAccount(
                    title = "Excluir conta",
                    icon = IconPaths.BANK_LOGOS + account.icon,
                    objectName = "${account.group.name}/${account.name}",
                    alertText = "Isso irá excluir permanentemente a conta ${account.group.name} → ${account.name}, bem como todas as transações associadas a ela.",
                    onClickButton = {
                        viewModel.accountService.deleteAccount(account)
                        viewModel.groupService.loadGroups()
                        onDismissRequest()
                    },
                    onDismiss = { onDismissRequest(); deleteDialog = false }
                )
        }
    }

}
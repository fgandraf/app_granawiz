package view.modules.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.*
import view.modules.accountForm.AccountForm
import view.modules.groupForm.GroupForm
import view.modules.sidebar.components.AccountType
import view.modules.sidebar.components.ButtonFooterItem
import viewModel.SidebarViewModel

@Composable
fun Footer(viewModel: SidebarViewModel) {

    Divider()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .background(Color.White, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
    ){

        var showNewGroupDialog by remember { mutableStateOf(false) }
        ButtonFooterItem(Modifier.weight(1f), PhosphorIcons.Light.Folders, "Novo grupo") { showNewGroupDialog = true }
        if (showNewGroupDialog) GroupForm(viewModel = viewModel, onDismiss = { showNewGroupDialog = false })

        Divider(modifier = Modifier.fillMaxHeight().width(1.dp))

        var showNewAccountMenu by remember { mutableStateOf(false) }
        ButtonFooterItem(
            modifier = Modifier.weight(1f),
            icon = PhosphorIcons.Light.Wallet,
            label = "Nova conta",
            enabled = viewModel.groups.isNotEmpty()
        ) {
            showNewAccountMenu = !showNewAccountMenu
        }
        DropDownNewAccount(viewModel, showNewAccountMenu) { showNewAccountMenu = false }

    }
}


@Composable
fun DropDownNewAccount(
    viewModel: SidebarViewModel,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {

    var showNewAccountDialog by remember { mutableStateOf(false) }
    var accountType by remember { mutableStateOf(core.enums.AccountType.CHECKING) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        DropdownMenu(
            modifier = Modifier.padding(30.dp).width(250.dp),
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                AccountType(
                    icon = PhosphorIcons.Light.Bank,
                    color = MaterialTheme.colors.primary,
                    label = "Conta Corrente",
                    onContainerClick = {
                        accountType = core.enums.AccountType.CHECKING
                        showNewAccountDialog = true
                        onDismissRequest()
                    }
                )

                AccountType(
                    icon = PhosphorIcons.Light.PiggyBank,
                    color = MaterialTheme.colors.primary,
                    label = "Conta Poupança",
                    onContainerClick = {
                        accountType = core.enums.AccountType.SAVINGS
                        showNewAccountDialog = true
                        onDismissRequest()
                    }
                )

                AccountType(
                    icon = PhosphorIcons.Light.CreditCard,
                    color = MaterialTheme.colors.primary,
                    label = "Cartão de Crédito",
                    onContainerClick = {
                        accountType = core.enums.AccountType.CREDIT_CARD
                        showNewAccountDialog = true
                        onDismissRequest()
                    }
                )

            }
        }
    }

    if (showNewAccountDialog)
        AccountForm(
            sidebarViewModel = viewModel,
            accountType = accountType,
            onDismiss = {
                showNewAccountDialog = false
                viewModel.fetchTotalAccounts()
            }
        )
}
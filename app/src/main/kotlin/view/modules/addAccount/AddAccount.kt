 package view.modules.addAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import config.IconPaths
import view.modules.addAccount.components.AccountType
import view.shared.ClickableIcon
import view.shared.TextPrimary
import viewModel.SidebarViewModel

 @Composable
fun AddAccount(
    viewModel: SidebarViewModel,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
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
                    text = "Adicionar nova conta",
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(top = 50.dp, bottom = 70.dp)
                    .width(300.dp)
            ) {

                AccountType(
                    icon = IconPaths.SYSTEM_ICONS + "checking.svg",
                    color = MaterialTheme.colors.primary,
                    label = "Conta Corrente",
                    onContainerClick = { }
                )

                AccountType(
                    icon = IconPaths.SYSTEM_ICONS + "savings.svg",
                    color = MaterialTheme.colors.primary,
                    label = "Conta Poupança",
                    onContainerClick = {}
                )

                AccountType(
                    icon = IconPaths.SYSTEM_ICONS + "investment.svg",
                    color = MaterialTheme.colors.primary,
                    label = "Conta de Investimentos",
                    onContainerClick = {}
                )

                AccountType(
                    icon = IconPaths.SYSTEM_ICONS + "credit_card.svg",
                    color = MaterialTheme.colors.primary,
                    label = "Cartão de Crédito",
                    onContainerClick = {}
                )

            }



            //ClickableRow(label = "Inserir conta de teste") { viewModel.insertTestAccountOnGroup() }



        }
    }
}
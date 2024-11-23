package view.modules.addAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import core.entity.account.BankAccount
import core.entity.account.CheckingAccount
import core.entity.account.CreditCardAccount
import core.entity.account.SavingsAccount
import core.enums.AccountType
import view.shared.DialogTitleBar
import viewModel.AddAccountViewModel
import viewModel.SidebarViewModel

@Composable
fun AddAccount(
    sidebarViewModel: SidebarViewModel,
    addAccountViewModel: AddAccountViewModel = AddAccountViewModel(),
    accountType: AccountType? = null,
    account: BankAccount? = null,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(500.dp).defaultMinSize(minHeight = 400.dp)
                .background(MaterialTheme.colors.background, shape = RoundedCornerShape(8.dp))
        ) {


            val type = account?.type ?: accountType
            var title = when (type!!) {
                AccountType.SAVINGS -> "conta poupança"
                AccountType.CHECKING -> "conta corrente"
                AccountType.CREDIT_CARD -> "cartão de crédito"
            }
            title = if (account == null) "Adiconar $title" else "Editar $title"


            //===== Title Bar
            DialogTitleBar(title, onDismiss)
            Divider()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (type) {
                    AccountType.SAVINGS -> NewOrEditSavingAccount(
                        sidebarViewModel = sidebarViewModel,
                        addAccountViewModel = addAccountViewModel,
                        account = if (account != null) account as SavingsAccount else null,
                        onDismiss = onDismiss
                    )

                    AccountType.CHECKING -> NewOrEditCheckingAccount(
                        sidebarViewModel = sidebarViewModel,
                        account = if (account != null) account as CheckingAccount else null,
                        addAccountViewModel = addAccountViewModel,
                        onDismiss = onDismiss
                    )

                    AccountType.CREDIT_CARD -> NewOrEditCreditCard(
                        sidebarViewModel = sidebarViewModel,
                        account = if (account != null) account as CreditCardAccount else null,
                        addAccountViewModel = addAccountViewModel,
                        onDismiss = onDismiss
                    )

                }
            }
        }
    }
}
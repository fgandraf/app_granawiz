package view.modules.accountForm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import domain.entity.account.BankAccount
import domain.entity.account.CheckingAccount
import domain.entity.account.CreditCardAccount
import domain.entity.account.SavingsAccount
import domain.enums.AccountType
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import view.shared.DialogTitleBar
import viewModel.AccountFormViewModel
import viewModel.SidebarViewModel

@Composable
fun AccountForm(
    sidebarViewModel: SidebarViewModel,
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
            val title = when (type!!) {
                AccountType.SAVINGS -> if (account == null) stringResource(Res.string.account_form_add_savings) else stringResource(Res.string.account_form_edit_savings)
                AccountType.CHECKING -> if (account == null) stringResource(Res.string.account_form_add_checking) else stringResource(Res.string.account_form_edit_checking)
                AccountType.CREDIT_CARD -> if (account == null) stringResource(Res.string.account_form_add_credit_card) else stringResource(Res.string.account_form_edit_credit_card)
            }


            //===== Title Bar
            DialogTitleBar(title = title, onCloseRequest = onDismiss)
            Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp).background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.2f)))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (type) {
                    AccountType.SAVINGS -> NewOrEditSavingAccount(
                        sidebarViewModel = sidebarViewModel,
                        accountFormViewModel = AccountFormViewModel(),
                        account = if (account != null) account as SavingsAccount else null,
                        onDismiss = onDismiss
                    )

                    AccountType.CHECKING -> NewOrEditCheckingAccount(
                        sidebarViewModel = sidebarViewModel,
                        accountFormViewModel = AccountFormViewModel(),
                        account = if (account != null) account as CheckingAccount else null,
                        onDismiss = onDismiss
                    )

                    AccountType.CREDIT_CARD -> NewOrEditCreditCard(
                        sidebarViewModel = sidebarViewModel,
                        accountFormViewModel = AccountFormViewModel(),
                        account = if (account != null) account as CreditCardAccount else null,
                        onDismiss = onDismiss
                    )

                }
            }
        }
    }
}
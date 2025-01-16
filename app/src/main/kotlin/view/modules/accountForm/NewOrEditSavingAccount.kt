package view.modules.accountForm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import core.entity.account.SavingsAccount
import core.enums.AccountType
import utils.toBrMoney
import view.modules.accountForm.components.IconSelector
import view.shared.DefaultButton
import view.shared.DefaultTextField
import view.modules.accountForm.components.GroupListComboBox
import viewModel.AccountFormViewModel
import viewModel.SidebarViewModel

@Composable
fun NewOrEditSavingAccount(
    sidebarViewModel: SidebarViewModel,
    accountFormViewModel: AccountFormViewModel,
    account: SavingsAccount? = null,
    onDismiss: () -> Unit,
) {

    if (account != null) {
        LaunchedEffect(account) { accountFormViewModel.initializeFromAccount(account) }
    }
    val buttonLabel by remember { mutableStateOf(if (account == null) "Adicionar" else "Editar") }

    accountFormViewModel.type = AccountType.SAVINGS

    Column(Modifier.fillMaxWidth().padding(top = 30.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        //==== ICON
        IconSelector(accountFormViewModel.icon) { accountFormViewModel.icon = it }

        //==== FORM
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 60.dp, end = 60.dp, top = 35.dp, bottom = 50.dp)
                .background(MaterialTheme.colors.surface, RoundedCornerShape(16.dp))
                .border(0.5.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(8.dp))
        ) {
            Column(Modifier.fillMaxWidth().padding(40.dp)) {

                //---name
                DefaultTextField(
                    modifier = Modifier.padding(bottom = 20.dp),
                    value = accountFormViewModel.name,
                    label = "Nome:",
                    placeholder = "Nome da conta"
                ) { accountFormViewModel.name = it }


                //---open balance
                var openBalanceText by remember { mutableStateOf(if (account != null) toBrMoney.format(account.openBalance) else "") }
                DefaultTextField(
                    modifier = Modifier.padding(bottom = 20.dp),
                    value = openBalanceText,
                    label = "Saldo inicial:",
                    textAlign = TextAlign.Right,
                    placeholder = "0.000,00"
                ) {
                    openBalanceText = it.filter { char -> char.isDigit() || char == ',' || char == '.' }
                    accountFormViewModel.balance = it.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                }

                //---group
                GroupListComboBox(
                    modifier = Modifier.padding(bottom = 20.dp),
                    value = accountFormViewModel.group.name,
                    label = "Grupo:",
                    placeholder = "Selecione o grupo",
                    groupList = sidebarViewModel.groups.value,
                    onClickItem = { accountFormViewModel.group = it }
                )

                //---description
                DefaultTextField(
                    value = accountFormViewModel.description,
                    label = "Descrição:",
                    boxSize = 80.dp,
                    placeholder = "Informações adicionais"
                ) { accountFormViewModel.description = it }
            }
        }


        //==== FOOTER
        Divider(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)
                .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.2f))
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            val confirmed by remember { derivedStateOf { accountFormViewModel.name != "" && accountFormViewModel.group.id != 0L } }

            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                confirmed = confirmed,
                text = buttonLabel,
                textColor = Color.White
            ) {
                accountFormViewModel.saveAccount()
                sidebarViewModel.reload()
                onDismiss()
            }
        }
    }

}
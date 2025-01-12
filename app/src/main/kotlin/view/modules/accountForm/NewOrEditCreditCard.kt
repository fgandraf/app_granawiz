package view.modules.accountForm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import core.entity.account.CreditCardAccount
import core.enums.AccountType
import utils.toBrMoney
import view.modules.accountForm.components.IconSelector
import view.shared.DefaultButton
import view.shared.DefaultTextField
import view.modules.accountForm.components.GroupListComboBox
import viewModel.AccountFormViewModel
import viewModel.SidebarViewModel

@Composable
fun NewOrEditCreditCard(
    sidebarViewModel: SidebarViewModel,
    accountFormViewModel: AccountFormViewModel,
    account: CreditCardAccount? = null,
    onDismiss: () -> Unit
){

    if (account != null) { LaunchedEffect(account) { accountFormViewModel.initializeFromAccount(account) } }
    val buttonLabel by remember { mutableStateOf(if (account == null) "Adicionar" else "Editar") }

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


                //---limite
                var limitText by remember { mutableStateOf(if (account != null) toBrMoney.format(account.creditLimit) else "") }
                DefaultTextField(
                    modifier = Modifier.padding(bottom = 20.dp),
                    value = limitText,
                    label = "Limite de crédito:",
                    textAlign = TextAlign.Right,
                    placeholder = "0.000,00"
                ) {
                    limitText = it.filter { char -> char.isDigit() || char == ',' || char == '.' }
                    accountFormViewModel.limit = it.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                }



                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                    //---closing day
                    var closingDayText by remember { mutableStateOf(account?.closingDay?.toString() ?: "") }
                    DefaultTextField(
                        modifier = Modifier.weight(1f).padding(end = 10.dp),
                        value = closingDayText,
                        label = "Dia do fechamento:",
                        textAlign = TextAlign.Right,
                        placeholder = "1 à 31"
                    ) {
                        val filteredInput = it.filter { char -> char.isDigit() }.take(2).toIntOrNull()
                        if (filteredInput != null && filteredInput in 1..31) {
                            accountFormViewModel.closingDay = filteredInput
                            closingDayText = filteredInput.toString()
                        }
                    }

                    //---due day
                    var dueDayText by remember { mutableStateOf(account?.dueDay?.toString() ?: "") }
                    DefaultTextField(
                        modifier = Modifier.weight(1f).padding(start = 10.dp),
                        value = dueDayText,
                        label = "Dia da fatura:",
                        textAlign = TextAlign.Right,
                        placeholder = "1 à 31"
                    ) {
                        val filteredInput = it.filter { char -> char.isDigit() }.take(2).toIntOrNull()
                        if (filteredInput != null && filteredInput in 1..31) {
                            accountFormViewModel.dueDay = filteredInput
                            dueDayText = filteredInput.toString()
                        }

                    }
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
        Divider()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            val confirmed by remember { derivedStateOf { accountFormViewModel.name != "" && accountFormViewModel.group.id != 0L } }

            DefaultButton(modifier = Modifier.fillMaxWidth(), confirmed = confirmed, text = buttonLabel, textColor = MaterialTheme.colors.surface) {
                accountFormViewModel.service.saveAccount(AccountType.CREDIT_CARD, account)
                sidebarViewModel.groupService.loadGroups()
                onDismiss()
            }
        }
    }

}
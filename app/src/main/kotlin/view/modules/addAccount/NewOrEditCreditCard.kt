package view.modules.addAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.entity.account.CreditCardAccount
import model.utils.toBrMoney
import view.modules.addAccount.components.IconSelector
import view.shared.DefaultButton
import view.shared.DefaultTextField
import view.shared.GroupListComboBox
import viewModel.AddAccountViewModel
import viewModel.SidebarViewModel

@Composable
fun NewOrEditCreditCard(
    sidebarViewModel: SidebarViewModel,
    addAccountViewModel: AddAccountViewModel,
    account: CreditCardAccount? = null,
    onDismiss: () -> Unit
){

    if (account != null) { LaunchedEffect(account) { addAccountViewModel.initializeFromAccount(account) } }
    val buttonLabel by remember { mutableStateOf(if (account == null) "Adicionar" else "Editar") }

    Column(Modifier.fillMaxWidth().padding(top = 30.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        //==== ICON
        IconSelector(addAccountViewModel.icon) { addAccountViewModel.icon = it }

        //==== FORM
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 60.dp, end = 60.dp, top = 35.dp, bottom = 50.dp)
                .background(MaterialTheme.colors.onPrimary, RoundedCornerShape(16.dp))
                .border(0.5.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(8.dp))
        ) {
            Column(Modifier.fillMaxWidth().padding(40.dp)) {

                //---name
                DefaultTextField(
                    modifier = Modifier.padding(bottom = 20.dp),
                    value = addAccountViewModel.name,
                    label = "Nome:",
                    placeholder = "Nome da conta"
                ) { addAccountViewModel.name = it }


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
                    addAccountViewModel.limit = it.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0
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
                            addAccountViewModel.closingDay = filteredInput
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
                            addAccountViewModel.dueDay = filteredInput
                            dueDayText = filteredInput.toString()
                        }

                    }
                }

                //---group
                GroupListComboBox(
                    modifier = Modifier.padding(bottom = 20.dp),
                    value = addAccountViewModel.group.name,
                    label = "Grupo:",
                    placeholder = "Selecione o grupo",
                    groupList = sidebarViewModel.groups,
                    onClickItem = { addAccountViewModel.group = it }
                )

                //---description
                DefaultTextField(
                    value = addAccountViewModel.description,
                    label = "Descrição:",
                    boxSize = 80.dp,
                    placeholder = "Informações adicionais"
                ) { addAccountViewModel.description = it }
            }
        }


        //==== FOOTER
        Divider()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            val confirmed by remember { derivedStateOf { addAccountViewModel.name != "" && addAccountViewModel.group.id != 0L } }

            DefaultButton(confirmed = confirmed, buttonLabel) {
                addAccountViewModel.saveCreditCardAccount(account)
                sidebarViewModel.loadGroup()
                onDismiss()
            }
        }
    }

}
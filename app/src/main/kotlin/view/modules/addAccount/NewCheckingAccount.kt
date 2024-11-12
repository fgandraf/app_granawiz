package view.modules.addAccount

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
import model.entity.Group
import model.entity.account.CheckingAccount
import view.modules.addAccount.components.IconSelector
import view.shared.DefaultButton
import view.shared.DefaultTextField
import view.shared.ListComboBox
import viewModel.AddAccountViewModel
import viewModel.SidebarViewModel

@Composable
fun NewCheckingAccount(
    sidebarViewModel: SidebarViewModel,
    addAccountViewModel: AddAccountViewModel,
    onDismiss: () -> Unit
){

    var name by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("default.svg") }
    var openBalance by remember { mutableStateOf(0.0) }
    var limit by remember { mutableStateOf(0.0) }
    var group by remember { mutableStateOf(Group()) }
    var description by remember { mutableStateOf("") }

    Column(Modifier.fillMaxWidth().padding(top = 30.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        //==== ICON
        IconSelector(icon) { icon = it }

        //==== FORM
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .padding(top = 35.dp, bottom = 50.dp)
                .background(MaterialTheme.colors.onPrimary, RoundedCornerShape(16.dp))
                .border(0.5.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(8.dp))
        ) {
            Column(Modifier.fillMaxWidth().padding(40.dp)) {

                //---name
                DefaultTextField(
                    modifier = Modifier.padding(bottom = 20.dp),
                    value = name,
                    label = "Nome:",
                    placeholder = "Nome da conta"
                ) { name = it }

                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                    //---open balance
                    var openBalanceText by remember { mutableStateOf("") }
                    DefaultTextField(
                        modifier = Modifier.weight(1f).padding(end = 10.dp),
                        value = openBalanceText,
                        label = "Saldo inicial:",
                        textAlign = TextAlign.Right,
                        placeholder = "0.000,00"
                    ) {
                        openBalanceText = it.filter { char -> char.isDigit() || char == ',' || char == '.' }
                        openBalance = it.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                    }

                    //---limit
                    var limitText by remember { mutableStateOf("") }
                    DefaultTextField(
                        modifier = Modifier.weight(1f).padding(start = 10.dp),
                        value = limitText,
                        label = "Limite:",
                        textAlign = TextAlign.Right,
                        placeholder = "0.000,00"
                    ) {
                        limitText = it.filter { char -> char.isDigit() || char == ',' || char == '.' }
                        limit = it.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                    }
                }

                //---group
                val expandedGroupItems = remember { mutableStateOf(false) }
                ListComboBox(
                    modifier = Modifier.padding(bottom = 20.dp),
                    value = group.name,
                    label = "Grupo:",
                    placeholder = "Selecione o grupo",
                    expanded = expandedGroupItems,
                    list = sidebarViewModel.groups,
                    onClickItem = { group = it; expandedGroupItems.value = false }
                )

                //---description
                DefaultTextField(
                    value = description,
                    label = "Descrição:",
                    boxSize = 80.dp,
                    placeholder = "Informações adicionais"
                ) { description = it }
            }
        }


        //==== FOOTER
        Divider()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            val confirmed by remember { derivedStateOf { name != "" && group.id != 0L } }

            DefaultButton(confirmed = confirmed, "Adicionar conta") {

                val account = CheckingAccount(
                    name = name,
                    description = description,
                    position = sidebarViewModel.groups.find { it.id == group.id }!!.accounts.size + 1,
                    icon = icon,
                    balance = openBalance,
                    group = group,
                    openBalance = openBalance,
                    overdraftLimit = limit
                )
                addAccountViewModel.addAccount(account)
                sidebarViewModel.loadGroup()
                onDismiss()
            }
        }
    }
}
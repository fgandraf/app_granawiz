package view.modules.addAccount

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.IconPaths
import model.entity.Group
import model.entity.account.CheckingAccount
import view.shared.ComboBoxField
import view.shared.DefaultButton
import view.shared.DefaultTextField
import view.shared.TextPrimary
import viewModel.AddAccountViewModel
import viewModel.SidebarViewModel

@OptIn(ExperimentalMaterialApi::class)
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

    var expandedIcons by remember { mutableStateOf(false) }


    Column(Modifier.fillMaxWidth().padding(top = 30.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        //==== ICONE
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(Color.White, CircleShape)
                .clip(CircleShape)
                .border(0.8.dp, MaterialTheme.colors.primaryVariant, CircleShape)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable{expandedIcons = !expandedIcons}
        ) {
            Box(modifier = Modifier
                .padding(15.dp)
            ){
                Image(
                    modifier = Modifier.size(60.dp),
                    painter = painterResource(IconPaths.BANK_LOGOS + icon),
                    contentDescription = "",
                    alpha = 0.5f)
                Image(
                    modifier = Modifier.size(14.dp).align(Alignment.BottomEnd),
                    painter = painterResource(IconPaths.SYSTEM_ICONS + "edit.svg"),
                    contentDescription = "",
                    alpha = 0.8f)
            }
            DropDownIcons(
                expanded = expandedIcons,
                onDismissRequest = { expandedIcons = false }
            )

        }

        //==== FORMULARIO
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
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                    TextPrimary(text = "Nome:", modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)
                    DefaultTextField(value = name) { name = it }
                }

                //---open balance
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                    var openBalanceText by remember { mutableStateOf("") }
                    Column(modifier = Modifier.weight(1f).padding(end = 10.dp)) {
                        TextPrimary(text = "Saldo inicial:", modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)
                        DefaultTextField(value = openBalanceText, textAlign = TextAlign.Right, placeholder = "0.000,00"){
                            openBalanceText = it.filter { char -> char.isDigit() || char == ',' || char == '.' }
                            openBalance = it.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                        }
                    }
                    var limitText by remember { mutableStateOf("") }
                    Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                        TextPrimary(text = "Limite:", modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)
                        DefaultTextField(value = limitText, textAlign = TextAlign.Right, placeholder = "0.000,00") {
                            limitText = it.filter { char -> char.isDigit() || char == ',' || char == '.' }
                            limit = it.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                        }
                    }
                }

                //---group
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                    TextPrimary(text = "Grupo:", modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)
                    var expanded by remember { mutableStateOf(false) }
                    var selectedGroupName by remember { mutableStateOf("") }
                    var focused by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        modifier = Modifier.onFocusChanged { state -> focused = state.isFocused },
                        expanded = expanded, onExpandedChange = { expanded = !expanded }) {

                        ComboBoxField(label = selectedGroupName, focused = focused, expanded = expanded)
                        ExposedDropdownMenu(
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                            expanded = expanded, onDismissRequest = { expanded = false }
                        ){
                            sidebarViewModel.groups.forEach { item ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedGroupName = item.name
                                        group = item
                                        expanded = false
                                    }
                                ) {
                                    TextPrimary(text = item.name, size = 12.sp)
                                }
                            }

                        }
                    }
                }

                //---description
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextPrimary(text = "Descrição:", modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)
                    DefaultTextField(value = description, boxSize = 80.dp) { description = it }
                }
            }
        }


        //==== FOOTER
        Divider()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            val confirmed by remember { derivedStateOf { name != "" && group.id != 0L } }

            DefaultButton(confirmed = confirmed, "Adicionar conta"){

                val account = CheckingAccount(
                    name = name,
                    description = description,
                    position = sidebarViewModel.groups.find{ it.id == group.id }!!.accounts.size + 1,
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


@Composable
fun DropDownIcons(
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        DropdownMenu(
            modifier = Modifier.weight(1f).padding(horizontal = 10.dp).width(300.dp).height(300.dp),
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {
            // IMPLEMENTS
        }
    }

}
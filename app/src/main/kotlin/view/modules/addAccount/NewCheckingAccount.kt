package view.modules.addAccount

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.IconPaths
import model.entity.Group
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
    addAccountViewModel: AddAccountViewModel
){

    var name by remember { mutableStateOf("") }
    var openBalance by remember { mutableStateOf(0.0) }
    var limit by remember { mutableStateOf(0.0) }
    var group by remember { mutableStateOf(Group()) }
    var description by remember { mutableStateOf("") }

    Column(Modifier.fillMaxWidth().padding(top = 30.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        //==== ICONE
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextPrimary(text = "Ícone:", modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)
            Box(modifier = Modifier.pointerHoverIcon(PointerIcon.Hand).clickable{
                // IMPLEMENTS
            }){
                Image(
                    modifier = Modifier.size(60.dp),
                    painter = painterResource(IconPaths.BANK_LOGOS + "default.svg"),
                    contentDescription = "",
                    alpha = 0.3f)
                Image(
                    modifier = Modifier.size(14.dp).align(Alignment.BottomEnd),
                    painter = painterResource(IconPaths.SYSTEM_ICONS + "edit.svg"),
                    contentDescription = "") }
        }



        //==== FORMULARIO
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp).padding(top = 35.dp, bottom = 50.dp)
        ) {

            //---name
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                TextPrimary(text = "Nome:", modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)
                DefaultTextField(value = name) { name = it }
            }

            //---open balance
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                Column(modifier = Modifier.weight(1f).padding(end = 10.dp)) {
                    TextPrimary(text = "Saldo inicial:", modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)
                    DefaultTextField(value = openBalance.toString(), textAlign = TextAlign.Right){ openBalance = it.toDouble() }
                }
                Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                    TextPrimary(text = "Limite:", modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)
                    DefaultTextField(value = limit.toString(), textAlign = TextAlign.Right) { limit = it.toDouble() }
                }
            }

            //---group
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                TextPrimary(text = "Grupo:", modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)
                var expanded by remember { mutableStateOf(false) }
                var selectedGroupName by remember { mutableStateOf("") }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    ComboBoxField(label = selectedGroupName, expanded = expanded)
                    ExposedDropdownMenu(
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                        expanded = expanded, onDismissRequest = { expanded = false }
                    ){
                        sidebarViewModel.groups.forEach { x ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedGroupName = x.name
                                    group = x
                                    expanded = false
                                }
                            ) {
                                TextPrimary(text = x.name, size = 12.sp)
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


        //==== FOOTER
        Divider()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            val confirmed by remember { derivedStateOf { name != "" && group.id != 0L } }

            DefaultButton(confirmed = confirmed, "Adicionar conta"){
                //IMPLEMENTS
            }
        }

    }

}
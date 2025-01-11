package view.modules.transactionForm.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.HandArrowDown
import com.adamglin.phosphoricons.light.HandArrowUp
import core.entity.Party
import core.entity.PartyName
import core.enums.PartyType
import view.shared.*
import viewModel.PartyViewModel


@Composable
fun PartiesPicker(
    partyType: PartyType,
    viewModel: PartyViewModel = remember { PartyViewModel(type = partyType) },
    party: Party? = null,
    partyName: PartyName? = null,
    onPartyClick: (Party) -> Unit
) {
    viewModel.selectedParty.value = party
    viewModel.selectedName.value = partyName
    viewModel.getParties()
    viewModel.getNames()

    val parties by viewModel.parties.collectAsState()
    val names by viewModel.partyNames.collectAsState()

    // EXTERNAL
    val corner = 10.dp
    Box(
        modifier = Modifier
            .width(600.dp)
            .background(MaterialTheme.colors.onPrimary, RoundedCornerShape(topEnd = corner, bottomEnd = corner))
            .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(topEnd = corner, bottomEnd = corner)),
        contentAlignment = Alignment.Center
    ) {

        // CONTENT BOX
        Row(modifier = Modifier.padding(vertical = 30.dp, horizontal = 10.dp)) {

            // PARTIES
            Box(modifier = Modifier.weight(1f)){
                val listState = rememberLazyListState()
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    items(parties, key = { it.id  }) { item -> AddPartyItem(viewModel, item) { onPartyClick(it) } }
                    item { AddPartyButton(viewModel) }
                }
                VerticalScrollbar(adapter = rememberScrollbarAdapter(listState), modifier = Modifier.align(Alignment.CenterEnd))
            }

            // DIVIDER
            Divider(modifier = Modifier.width(2.dp).fillMaxHeight())

            // PARTYNAMES
            Box(modifier = Modifier.weight(1f)){
                val listState = rememberLazyListState()
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    item { Spacer(Modifier.height(10.dp)) }
                    item { TextPrimary(modifier = Modifier.padding(start = 20.dp, bottom = 10.dp), text = "Nomes associados:") }
                    items(names, key = { it.id }) { item -> AddAssociatedNamesItem(viewModel, item) }
                    item { AddNameButton(viewModel) }
                }
                VerticalScrollbar(adapter = rememberScrollbarAdapter(listState), modifier = Modifier.align(Alignment.CenterEnd))
            }

        }

    }

}



@Composable
fun AddPartyItem(
    viewModel: PartyViewModel,
    item: Party,
    onClick: (Party) -> Unit,
){
    val deleteDialogIsVisible = remember { mutableStateOf(false) }
    ListItem(
        label = item.name,
        hasSubItem = item.partiesNames.size > 0,
        isActive = item.id == viewModel.selectedParty.value?.id,
        deleteDialogIsVisible = deleteDialogIsVisible,
        onUpdateConfirmation = { viewModel.updateParty(item, it)},
        onContentClick = {
            viewModel.selectedParty.value = item
            viewModel.getNames()
            onClick(item)
        },
        deleteDialog = {
            val type = if (viewModel.selectedType.value == PartyType.RECEIVER) "recebedor" else "pagador"
            val iconResource = if (viewModel.selectedType.value == PartyType.RECEIVER) PhosphorIcons.Light.HandArrowDown else PhosphorIcons.Light.HandArrowUp

            DialogDelete(
                title = "Excluir $type",
                icon = iconResource,
                objectName = item.name,
                alertText = "Isso irá excluir permanentemente o $type ${item.name} e remover todas as associações feitas à ele.",
                onClickButton = { viewModel.deleteParty(item) },
                onDismiss = { deleteDialogIsVisible.value = false }
            )
        }
    )
}

@Composable
fun AddPartyButton(viewModel: PartyViewModel){
    val value = remember { mutableStateOf("") }
    val isVisible = remember { mutableStateOf(false) }
    AddListItem(
        isVisible = isVisible,
        value = value,
        confirmationClick = { viewModel.addParty(name = value.value) }
    )
}

@Composable
fun AddAssociatedNamesItem(
    viewModel: PartyViewModel,
    item: PartyName
){
    val deleteDialogIsVisible = remember { mutableStateOf(false) }
    ListItem(
        label = item.name,
        hasSubItem = false,
        isActive = viewModel.selectedName.value?.id == item.id,
        spaceBetween = 0.dp,
        deleteDialogIsVisible = deleteDialogIsVisible,
        onUpdateConfirmation = { viewModel.updatePartyName(item, it) },
        onContentClick = null,
        deleteDialog = {
            DialogDelete(
                title = "Excluir nome",
                icon = PhosphorIcons.Light.HandArrowUp,
                objectName = item.name,
                alertText = "Isso irá excluir permanentemente o nome ${item.name} e remover todas as associações feitas à ele.",
                onClickButton = { viewModel.deleteName(item) },
                onDismiss = { deleteDialogIsVisible.value = false }
            )
        }
    )
}

@Composable
fun AddNameButton(viewModel: PartyViewModel){
    val value = remember { mutableStateOf("") }
    val isVisible = remember { mutableStateOf(false) }
    AddListItem(
        isVisible = isVisible,
        value = value,
        confirmationClick = { viewModel.addName(value.value) },
        alertDialogContent = {

            viewModel.errorMessage.value.let { message ->
                if (message != null) {
                    SimpleAlertDialog(
                        onDismissRequest = { viewModel.clearError() },
                        title = "Associação já existente",
                        message = message
                    )
                }
            }
        }
    )
}
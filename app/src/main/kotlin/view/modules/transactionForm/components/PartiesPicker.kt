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

    val selectedParty by viewModel.selectedParty.collectAsState()
    val selectedName by viewModel.selectedName.collectAsState()

    val corner = 30.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onPrimary, RoundedCornerShape(topEnd = corner, bottomEnd = corner))
            .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(topEnd = corner, bottomEnd = corner))
    ) {

        Row(modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)) {

            //===== PARTY
            Row(modifier = Modifier.width(300.dp).fillMaxHeight()) {

                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().padding(vertical = 10.dp)
                ) {
                    val listState = rememberLazyListState()


                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {

                        items(parties, key = { it.id  }) { party ->

                            val deleteDialogIsVisible = remember { mutableStateOf(false) }
                            ListItem(
                                label = party.name,
                                hasSubItem = party.partiesNames.size > 0,
                                isActive = party.id == selectedParty?.id,
                                deleteDialogIsVisible = deleteDialogIsVisible,
                                onUpdateConfirmation = { viewModel.updateParty(party, it)},
                                onContentClick = {
                                    viewModel.selectedParty.value = party
                                    viewModel.getNames()
                                    onPartyClick(party)
                                },
                                deleteDialog = {
                                    val type = if (viewModel.selectedType.value == PartyType.RECEIVER) "recebedor" else "pagador"
                                    val iconResource = if (viewModel.selectedType.value == PartyType.RECEIVER) PhosphorIcons.Light.HandArrowDown else PhosphorIcons.Light.HandArrowUp

                                    DialogDelete(
                                        title = "Excluir $type",
                                        icon = iconResource,
                                        objectName = party.name,
                                        alertText = "Isso irá excluir permanentemente o $type ${party.name} e remover todas as associações feitas à ele.",
                                        onClickButton = { viewModel.deleteParty(party) },
                                        onDismiss = { deleteDialogIsVisible.value = false }
                                    )
                                }
                            )

                        }

                        item {
                            val value = remember { mutableStateOf("") }
                            val isVisible = remember { mutableStateOf(false) }
                            AddListItem(
                                isVisible = isVisible,
                                value = value,
                                confirmationClick = { viewModel.addParty(name = value.value) }
                            )
                        }

                    }
                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(listState),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            Divider(modifier = Modifier.width(2.dp).fillMaxHeight())


            //===== PARTYNAMES
            Row(modifier = Modifier.weight(1f).fillMaxHeight()) {

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 10.dp)
                ) {

                    Spacer(Modifier.height(20.dp))

                    val listState = rememberLazyListState()


                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {

                        item {
                            TextPrimary(modifier = Modifier.padding(start = 20.dp, bottom = 10.dp), text = "Nomes associados:")
                        }

                        items(names, key = { it.id }) { payerName ->

                            val deleteDialogIsVisible = remember { mutableStateOf(false) }
                            ListItem(
                                label = payerName.name,
                                hasSubItem = false,
                                isActive = selectedName?.id == payerName.id,
                                spaceBetween = 0.dp,
                                deleteDialogIsVisible = deleteDialogIsVisible,
                                onUpdateConfirmation = { viewModel.updatePartyName(payerName, it) },
                                onContentClick = null,
                                deleteDialog = {
                                    DialogDelete(
                                        title = "Excluir nome",
                                        icon = PhosphorIcons.Light.HandArrowUp,
                                        objectName = payerName.name,
                                        alertText = "Isso irá excluir permanentemente o nome ${payerName.name} e remover todas as associações feitas à ele.",
                                        onClickButton = { viewModel.deleteName(payerName) },
                                        onDismiss = { deleteDialogIsVisible.value = false }
                                    )
                                }
                            )
                        }

                        item {
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

                    }
                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(listState),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }

        }
    }
}
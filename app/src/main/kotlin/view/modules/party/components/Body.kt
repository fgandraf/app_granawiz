package view.modules.party.components

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.HandArrowDown
import com.adamglin.phosphoricons.light.HandArrowUp
import core.enums.PartyType
import view.shared.*
import viewModel.PartyViewModel

@Composable
fun Body(viewModel: PartyViewModel) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth(0.75f)
                .shadow(elevation = 1.dp, shape = RoundedCornerShape(20.dp))
                .border(0.5.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colors.onPrimary)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(35.dp)
            ) {
                val listState = rememberLazyListState()

                LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(top = 30.dp)) {

                    items(viewModel.parties.value, key = { it.id }) { party ->

                        val deleteDialogIsVisible = remember { mutableStateOf(false) }
                        ListItem(
                            label = party.name,
                            hasSubItem = party.partiesNames.size > 0,
                            deleteDialogIsVisible = deleteDialogIsVisible,
                            onUpdateConfirmation = { viewModel.service.updateParty(party, it)},
                            onContentClick = {
                                viewModel.service.loadNames(party)
                                viewModel.selectedParty.value = party
                                Unit
                            },
                            deleteDialog = {
                                val type = if (viewModel.selectedType.value == PartyType.RECEIVER) "recebedor" else "pagador"
                                val iconResource = if (viewModel.selectedType.value == PartyType.RECEIVER) PhosphorIcons.Light.HandArrowDown else PhosphorIcons.Light.HandArrowUp

                                DialogDelete(
                                    title = "Excluir $type",
                                    icon = iconResource,
                                    objectName = party.name,
                                    alertText = "Isso irá excluir permanentemente o $type ${party.name} e remover todas as associações feitas à ele.",
                                    onClickButton = { viewModel.service.deleteParty(party) },
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
                            confirmationClick = { viewModel.service.addParty(name = value.value, type = viewModel.selectedType.value) }
                        )
                    }

                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(listState),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            Divider(Modifier.fillMaxHeight(0.95f).width(1.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(35.dp)
            ) {
                TextPrimary(text = "Nomes associados:")

                val listState = rememberLazyListState()

                LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(top = 30.dp)) {

                    items(viewModel.partyNames.value, key = { it.id }) { payerName ->

                        val deleteDialogIsVisible = remember { mutableStateOf(false) }
                        ListItem(
                            label = payerName.name,
                            hasSubItem = false,
                            spaceBetween = 0.dp,
                            deleteDialogIsVisible = deleteDialogIsVisible,
                            onUpdateConfirmation = { viewModel.service.updatePartyName(payerName, it) },
                            onContentClick = null,
                            deleteDialog = {
                                DialogDelete(
                                    title = "Excluir nome",
                                    icon = PhosphorIcons.Light.HandArrowUp,
                                    objectName = payerName.name,
                                    alertText = "Isso irá excluir permanentemente o nome ${payerName.name} e remover todas as associações feitas à ele.",
                                    onClickButton = { viewModel.service.deletePartyName(payerName) },
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
                            confirmationClick = { viewModel.service.addPartyName(value.value, viewModel.selectedParty.value) },
                            alertDialogContent = {

                                viewModel.errorMessage.value.let { message ->
                                    if (message != null) {
                                        SimpleAlertDialog(
                                            onDismissRequest = { viewModel.service.clearError() },
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
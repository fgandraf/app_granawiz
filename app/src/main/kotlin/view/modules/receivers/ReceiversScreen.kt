package view.modules.receivers

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import config.IconPaths
import view.shared.AddListItem
import view.shared.ListItem
import view.shared.AddressView
import view.shared.DialogDelete
import view.shared.SearchBar
import view.shared.TextPrimary
import viewModel.ReceiverViewModel

@Composable
fun ReceiversScreen(
    receiverViewModel: ReceiverViewModel = ReceiverViewModel()
) {

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        //===== HEADER
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row { AddressView(IconPaths.SYSTEM_ICONS + "receiver.svg", "Beneficiários") }
                SearchBar(onTuneClicked = { /* TO DO */ }, onSearchClicked = { /* TO DO */ })
            }
        }

        //===== BODY
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .fillMaxWidth(0.7f)
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

                        items(receiverViewModel.receivers, key = { it.id }) { receiver ->

                            val deleteDialogIsVisible = remember { mutableStateOf(false) }
                            ListItem(
                                label = receiver.name,
                                hasSubItem = receiver.receiverNames.size > 0,
                                deleteDialogIsVisible = deleteDialogIsVisible,
                                onRenameConfirmation = { receiverViewModel.updateReceiver(receiver, it)},
                                onContentClick = { receiverViewModel.loadAssociatedNames(receiver) },
                                deleteDialog = {
                                    DialogDelete(
                                        title = "Excluir recebedor",
                                        iconResource = IconPaths.SYSTEM_ICONS + "receiver.svg",
                                        objectName = receiver.name,
                                        alertText = "Isso irá excluir permanentemente o recebedor ${receiver.name} e remover todas as associações feitas à ele.",
                                        onClickButton = { receiverViewModel.deleteReceiver(receiver) },
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
                                confirmationClick = { receiverViewModel.addReceiver(value.value) }
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

                        items(receiverViewModel.associatedReceiversName, key = { it.id }) { receiverName ->


                            val deleteDialogIsVisible = remember { mutableStateOf(false) }
                            ListItem(
                                label = receiverName.name,
                                hasSubItem = false,
                                spaceBetween = 0.dp,
                                deleteDialogIsVisible = deleteDialogIsVisible,
                                onRenameConfirmation = { /*receiverViewModel.updateReceiver(receiver, it)*/ },
                                onContentClick = null,
                                deleteDialog = {
                                    DialogDelete(
                                        title = "Excluir nome",
                                        iconResource = IconPaths.SYSTEM_ICONS + "receiver.svg",
                                        objectName = receiverName.name,
                                        alertText = "Isso irá excluir permanentemente o nome ${receiverName.name} e remover todas as associações feitas à ele.",
                                        onClickButton = { /*receiverViewModel.deleteReceiver(receiver)*/ },
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
                                confirmationClick = { /*receiverViewModel.addReceiver(value.value)*/ }
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
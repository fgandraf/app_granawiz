package view.modules.payers

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
import config.IconPaths
import view.shared.*
import viewModel.PayerViewModel

@Composable
fun PayersScreen(
    payerViewModel: PayerViewModel = PayerViewModel(),
) {

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        //===== HEADER
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row { AddressView(IconPaths.SYSTEM_ICONS + "payer.svg", "Pagadores") }
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

                        items(payerViewModel.payers, key = { it.id }) { payer ->

                            val deleteDialogIsVisible = remember { mutableStateOf(false) }
                            ListItem(
                                label = payer.name,
                                hasSubItem = payer.payersNames.size > 0,
                                deleteDialogIsVisible = deleteDialogIsVisible,
                                onRenameConfirmation = { payerViewModel.updatePayer(payer, it)},
                                onContentClick = {
                                    payerViewModel.loadAssociatedNames(payer)
                                    payerViewModel.selectPayer(payer)
                                },
                                deleteDialog = {
                                    DialogDelete(
                                        title = "Excluir pagador",
                                        iconResource = IconPaths.SYSTEM_ICONS + "payer.svg",
                                        objectName = payer.name,
                                        alertText = "Isso irá excluir permanentemente o pagador ${payer.name} e remover todas as associações feitas à ele.",
                                        onClickButton = { payerViewModel.deletePayer(payer) },
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
                                confirmationClick = { payerViewModel.addPayer(value.value) }
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

                        items(payerViewModel.associatedPayersName, key = { it.id }) { payerName ->

                            val deleteDialogIsVisible = remember { mutableStateOf(false) }
                            ListItem(
                                label = payerName.name,
                                hasSubItem = false,
                                spaceBetween = 0.dp,
                                deleteDialogIsVisible = deleteDialogIsVisible,
                                onRenameConfirmation = { payerViewModel.updatePayerName(payerName, it) },
                                onContentClick = null,
                                deleteDialog = {
                                    DialogDelete(
                                        title = "Excluir nome",
                                        iconResource = IconPaths.SYSTEM_ICONS + "payer.svg",
                                        objectName = payerName.name,
                                        alertText = "Isso irá excluir permanentemente o nome ${payerName.name} e remover todas as associações feitas à ele.",
                                        onClickButton = { payerViewModel.deletePayerName(payerName) },
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
                                confirmationClick = { payerViewModel.addPayerName(value.value) },
                                alertDialogContent = {
                                    payerViewModel.errorMessage?.let { errorMessage ->
                                        SimpleAlertDialog(
                                            onDismissRequest = { payerViewModel.clearError() },
                                            title = "Associação já existente",
                                            message = errorMessage
                                        )
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
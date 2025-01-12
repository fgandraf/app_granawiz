package view.modules.transactions

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.bold.ListBullets
import com.adamglin.phosphoricons.fill.Pencil
import com.adamglin.phosphoricons.fill.Plus
import com.adamglin.phosphoricons.light.Plus
import com.adamglin.phosphoricons.regular.Folders
import com.adamglin.phosphoricons.regular.Wallet
import core.entity.Transaction
import core.entity.account.BankAccount
import core.enums.TransactionType
import core.structs.PageAddress
import view.modules.transactionForm.TransactionForm
import view.modules.transactions.component.DropDownAddTransaction
import view.modules.transactions.component.MonthHeader
import view.modules.transactions.component.TotalFooter
import view.modules.transactions.component.TransactionRow
import view.shared.AddressView
import view.shared.ClickableIcon
import view.shared.TextPrimary
import view.theme.ButtonPurple
import viewModel.TransactionViewModel


@Composable
fun TransactionsScreen(
    account: BankAccount? = null,
    showAddButton: Boolean = true,
    viewModel: TransactionViewModel = TransactionViewModel(account),
) {

    val initialAddress =
        if (account != null)
            listOf(
                PageAddress(iconVector = PhosphorIcons.Regular.Folders, iconSize = DpSize(21.dp, 18.dp), name = account.group.name, rootPath = true),
                PageAddress(iconVector = PhosphorIcons.Regular.Wallet, iconSize = DpSize(21.dp, 18.dp), name = account.name))
        else
            listOf(PageAddress(iconVector = PhosphorIcons.Bold.ListBullets, iconSize = DpSize(21.dp, 18.dp), name = "Todas as transações", rootPath = true))


    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }
    var showEditTransaction by remember { mutableStateOf(false) }
    var backIcon by remember { mutableStateOf(false) }
    var showTransactionsList by remember { mutableStateOf(true) }

    var addresses by remember { mutableStateOf(emptyList<PageAddress>()) }
    LaunchedEffect(account) {
        addresses = initialAddress
        showEditTransaction = false
        selectedTransaction = null
        backIcon = false
        showTransactionsList = true
    }

    var transactionType by remember { mutableStateOf(selectedTransaction?.type) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        // ********** HEADER **********
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ){
            // address
            Column{
                Row(Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    ClickableIcon(enabled = backIcon, icon = PhosphorIcons.Bold.ArrowLeft, iconSize = 22.dp, boxSize = 25.dp){
                        addresses = initialAddress
                        selectedTransaction = null
                        showEditTransaction = false
                        backIcon = false
                        showTransactionsList = true
                    }
                    Spacer(Modifier.width(10.dp))
                    addresses.forEach { AddressView(icon = it.iconVector, iconSize = it.iconSize!!, value = it.name, rootPath = it.rootPath) }
                }
                TextPrimary(text = account?.description ?: "", size = 12.sp, align = TextAlign.Start)
            }

            // TODO: Imoplements Searchbar
            //SearchBar(onTuneClicked = {}, onSearchClicked = {})
        }



        // ********** BODY **********
        if (showTransactionsList){
            Box(modifier = Modifier.fillMaxSize()) {
                val listState = rememberLazyListState()

                LazyColumn(state = listState, modifier = Modifier.fillMaxSize(),) {
                    val monthTransactions = viewModel.transactions.value.groupBy { it.date.month }
                    item { Spacer(modifier = Modifier.height(30.dp)) }

                    monthTransactions.forEach { (month, transactions) ->

                        item {
                            MonthHeader(modifier = Modifier.zIndex(1f), month = month)
                            var negativeBalnce = 0.0
                            var positiveBalance = 0.0
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 90.dp)
                                    .zIndex(2f)
                                    .clip(RoundedCornerShape(topEnd = 0.dp, bottomStart = 0.dp))
                                    .background(MaterialTheme.colors.surface, RoundedCornerShape(topEnd = 0.dp, bottomStart = 0.dp))
                                    .border(0.5.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(topEnd = 0.dp, bottomStart = 0.dp))
                            ) {
                                Spacer(Modifier.height(20.dp))
                                transactions.forEach { transaction ->
                                    if (transaction.balance >= 0) positiveBalance += transaction.balance
                                    else negativeBalnce -= transaction.balance
                                    TransactionRow(
                                        viewModel = viewModel,
                                        transaction = transaction,
                                        onClick = {
                                            addresses = addresses + PageAddress(
                                                iconVector = PhosphorIcons.Fill.Pencil,
                                                iconSize = DpSize(21.dp, 18.dp),
                                                name = "Editar " + if (transaction.type == TransactionType.GAIN) "receita" else "despesa"
                                            )
                                            selectedTransaction = transaction
                                            showEditTransaction = true
                                            backIcon = true
                                            showTransactionsList = false
                                        }
                                    )
                                }
                                Spacer(Modifier.height(20.dp))
                            }

                            TotalFooter(modifier = Modifier.zIndex(1f), incomeBalance = positiveBalance, outcomeBalance = negativeBalnce)
                            Spacer(Modifier.height(30.dp))
                        }

                    }
                    item { Spacer(Modifier.height(50.dp)) }

                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(listState),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
                if (showAddButton)
                    AddTransactionButton(
                        onClickGain = {
                            transactionType = TransactionType.GAIN
                            showTransactionsList = false
                            showEditTransaction = true
                            addresses = addresses + PageAddress(
                                iconVector = PhosphorIcons.Fill.Plus,
                                iconSize = DpSize(21.dp, 18.dp),
                                name = "Nova receita"
                            )
                            backIcon = true
                        },
                        onClickExpense = {
                            transactionType = TransactionType.EXPENSE
                            showTransactionsList = false
                            showEditTransaction = true

                            addresses = addresses + PageAddress(
                                iconVector = PhosphorIcons.Fill.Plus,
                                iconSize = DpSize(21.dp, 18.dp),
                                name = "Nova despesa"
                            )
                            backIcon = true
                        },
                        onDismiss = {
                            selectedTransaction = null
                            showEditTransaction = false
                            showTransactionsList = true
                            backIcon = false
                        }
                    )


            }
        }
        if (showEditTransaction) {
            if (viewModel.selectedAccount == null) viewModel.selectAccount(selectedTransaction!!.account)
            TransactionForm(
                account = viewModel.selectedAccount!!,
                transaction = selectedTransaction,
                transactionType = transactionType,
                onDismiss = {
                    backIcon = false
                    showEditTransaction = false
                    showTransactionsList = true
                    addresses = initialAddress
                    selectedTransaction = null
                }
            )
        }
    }
}



@Composable
fun AddTransactionButton(
    onClickGain: () -> Unit,
    onClickExpense: () -> Unit,
    onDismiss: () -> Unit
) {
    var showAddTransactionDropDownMenu by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize().padding(bottom = 50.dp, end = 50.dp)) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(ButtonPurple)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showAddTransactionDropDownMenu = true }
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.Center),
                imageVector = PhosphorIcons.Light.Plus,
                contentDescription = "Add transaction",
                tint = Color.White
            )
            if (showAddTransactionDropDownMenu) {
                DropDownAddTransaction(
                    expanded = showAddTransactionDropDownMenu,
                    onClickGain = onClickGain,
                    onClickExpense = onClickExpense,
                    onDismissRequest = {
                        onDismiss()
                        showAddTransactionDropDownMenu = false
                    }
                )
            }
        }
    }
}
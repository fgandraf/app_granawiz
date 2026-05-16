package view.modules.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.bold.ListBullets
import com.adamglin.phosphoricons.regular.*
import com.felipegandra.generated.resources.*
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.TransactionType
import domain.structs.PageAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import view.modules.Screen
import view.modules.transactionForm.TransactionForm
import view.modules.transactions.component.*
import view.shared.AddressView
import view.shared.CircleButton
import view.shared.ClickableIcon
import view.shared.FilterTransactionBar
import view.shared.SearchField
import view.shared.TextH2
import view.shared.TextNormal
import viewModel.TransactionViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.time.LocalDate
import java.time.Month


@Composable
fun TransactionsScreen(
    account: BankAccount? = null,
    showAddButton: Boolean = true,
    viewModel: TransactionViewModel = remember(account) { TransactionViewModel(account) },
    onScreenChange: (Screen) -> Unit = {},
    onSidebarReload: () -> Unit = {},
) {

    val initialAddress =
        if (account != null)
            listOf(
                PageAddress(
                    iconVector = PhosphorIcons.Regular.Folders,
                    iconSize = DpSize(21.dp, 18.dp),
                    name = account.group.name,
                    rootPath = true
                ),
                PageAddress(
                    iconVector = PhosphorIcons.Regular.Wallet,
                    iconSize = DpSize(21.dp, 18.dp),
                    name = account.name
                )
            )
        else
            listOf(
                PageAddress(
                    iconVector = PhosphorIcons.Bold.ListBullets,
                    iconSize = DpSize(21.dp, 18.dp),
                    name = stringResource(Res.string.nav_all_transactions),
                    rootPath = true
                )
            )

    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }
    var showEditTransaction by remember { mutableStateOf(false) }
    var backIcon by remember { mutableStateOf(false) }
    var showTransactionsList by remember { mutableStateOf(true) }
    var addresses by remember { mutableStateOf(emptyList<PageAddress>()) }

    val strTransactionEditIncome = stringResource(Res.string.transaction_edit_income)
    val strTransactionEditExpense = stringResource(Res.string.transaction_edit_expense)
    val strTransactionNewIncome = stringResource(Res.string.transaction_new_income)
    val strTransactionNewExpense = stringResource(Res.string.transaction_new_expense)

    LaunchedEffect(account) {
        addresses = initialAddress
        showEditTransaction = false
        selectedTransaction = null
        backIcon = false
        showTransactionsList = true
        viewModel.clearFilters()
    }

    val transactionsState by viewModel.transactions.collectAsState()
    val filters by viewModel.filters.collectAsState()

    var transactionType by remember { mutableStateOf<TransactionType?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 15.dp, bottom = 15.dp)
            .border(1.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colors.surface)
    ) {

        Header(
            backIcon = backIcon,
            addresses = addresses,
            showTransactionsList = showTransactionsList,
            hasTransactions = transactionsState.isNotEmpty(),
            searchQuery = filters.searchQuery,
            account = account,
            onBackClick = {
                addresses = initialAddress
                selectedTransaction = null
                showEditTransaction = false
                backIcon = false
                showTransactionsList = true
            },
            onSearchQueryChange = { viewModel.updateFilters { copy(searchQuery = it) } }
        )

        if (showTransactionsList) {
            Body(
                showAddButton = showAddButton,
                viewModel = viewModel,
                account = account,
                onTransactionClick = { transaction ->
                    addresses = addresses + PageAddress(
                        iconVector = PhosphorIcons.Regular.Pencil,
                        iconSize = DpSize(21.dp, 18.dp),
                        name = if (transaction.type == TransactionType.GAIN) strTransactionEditIncome else strTransactionEditExpense
                    )
                    selectedTransaction = transaction
                    showEditTransaction = true
                    backIcon = true
                    showTransactionsList = false
                },
                onTransactionDeleted = onSidebarReload,
                onAddGain = {
                    transactionType = TransactionType.GAIN
                    showTransactionsList = false
                    showEditTransaction = true
                    addresses = addresses + PageAddress(
                        iconVector = PhosphorIcons.Regular.PlusSquare,
                        iconSize = DpSize(21.dp, 18.dp),
                        name = strTransactionNewIncome
                    )
                    backIcon = true
                },
                onAddExpense = {
                    transactionType = TransactionType.EXPENSE
                    showTransactionsList = false
                    showEditTransaction = true
                    addresses = addresses + PageAddress(
                        iconVector = PhosphorIcons.Regular.MinusSquare,
                        iconSize = DpSize(21.dp, 18.dp),
                        name = strTransactionNewExpense
                    )
                    backIcon = true
                },
                onImport = { onScreenChange(Screen.ImportStatement(account = viewModel.selectedAccount)) },
                onDismissAdd = {
                    selectedTransaction = null
                    showEditTransaction = false
                    showTransactionsList = true
                    backIcon = false
                },
            )
        }

        if (showEditTransaction) {
            val tx = selectedTransaction
            if (tx != null && viewModel.selectedAccount == null) viewModel.selectAccount(tx.account)
            TransactionForm(
                transaction = selectedTransaction,
                transactionType = transactionType,
                initialAccount = viewModel.selectedAccount,
                lockAccount = true,
                onDismiss = { saved ->
                    backIcon = false
                    showEditTransaction = false
                    showTransactionsList = true
                    addresses = initialAddress
                    selectedTransaction = null
                    if (saved) {
                        viewModel.getTransactions()
                        viewModel.selectedAccount?.let { acc ->
                            val calculated = viewModel.transactions.value.sumOf { it.balance }
                            viewModel.updateBalance(acc.id, calculated)
                        }
                        onSidebarReload()
                    }
                }
            )
        }
    }
}

@Composable
private fun Header(
    backIcon: Boolean,
    addresses: List<PageAddress>,
    showTransactionsList: Boolean,
    hasTransactions: Boolean,
    searchQuery: String,
    account: BankAccount?,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 20.dp, end = 20.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(30.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            // address row
            Row(verticalAlignment = Alignment.CenterVertically) {
                ClickableIcon(
                    enabled = backIcon,
                    icon = PhosphorIcons.Bold.ArrowLeft,
                    iconSize = 22.dp,
                    boxSize = 25.dp
                ) {
                    onBackClick()
                }
                Spacer(Modifier.width(10.dp))
                addresses.forEach {
                    AddressView(
                        icon = it.iconVector,
                        iconSize = it.iconSize!!,
                        value = it.name,
                        rootPath = it.rootPath
                    )
                }
            }

            if (showTransactionsList && hasTransactions)
                SearchField(
                    value = searchQuery,
                    onValueChange = { onSearchQueryChange(it) }
                )
        }

        if (account?.description?.isNotEmpty() == true) {
            TextNormal(
                text = account.description,
                align = TextAlign.Start,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun Body(
    showAddButton: Boolean,
    viewModel: TransactionViewModel,
    account: BankAccount?,
    onTransactionClick: (Transaction) -> Unit,
    onTransactionDeleted: () -> Unit,
    onAddGain: () -> Unit,
    onAddExpense: () -> Unit,
    onImport: () -> Unit,
    onDismissAdd: () -> Unit,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var showAddDropDown by remember { mutableStateOf(false) }
    val strExportDialogTitle = stringResource(Res.string.transaction_export_dialog_title)
    val transactionsState by viewModel.transactions.collectAsState()
    val displayedTransactions by viewModel.displayedTransactions.collectAsState(emptyList())
    val filters by viewModel.filters.collectAsState()
    val availableYears by viewModel.availableYears.collectAsState(emptyList())

    Row(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (displayedTransactions.isEmpty())
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        TextH2(text = stringResource(Res.string.transactions_empty))
                    }

                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    val monthTransactions = displayedTransactions.groupBy { it.date.month }
                    item { Spacer(modifier = Modifier.height(30.dp)) }
                    monthTransactions.forEach { (month, transactions) ->
                        item {
                            MonthSection(
                                month = month,
                                transactions = transactions,
                                onTransactionClick = onTransactionClick,
                                onTransactionDelete = { tx ->
                                    viewModel.deleteTransaction(tx)
                                    onTransactionDeleted()
                                },
                            )
                        }
                    }
                    item { Spacer(Modifier.height(50.dp)) }
                }

                if (showAddButton)
                    CircleButton(onClick = { showAddDropDown = true }) {
                        if (showAddDropDown) {
                            DropDownAddTransaction(
                                expanded = showAddDropDown,
                                onClickGain = onAddGain,
                                onClickExpense = onAddExpense,
                                onClickImport = {
                                    showAddDropDown = false
                                    onImport()
                                },
                                onDismissRequest = {
                                    onDismissAdd()
                                    showAddDropDown = false
                                }
                            )
                        }
                    }
            }
        }

        FilterTransactionBar(
            listState = listState,
            visible = transactionsState.isNotEmpty(),
            items = viewModel.transactions,
            searchQuery = filters.searchQuery,
            currentAccountView = account,
            filterAccount = filters.account,
            onFilterAccountChange = { viewModel.updateFilters { copy(account = it) } },
            filterType = filters.type,
            onFilterTypeChange = { viewModel.updateFilters { copy(type = it) } },
            filterCategoryItem = filters.categoryItem,
            onFilterCategoryItemChange = { viewModel.updateFilters { copy(categoryItem = it) } },
            filterTag = filters.tag,
            onFilterTagChange = { viewModel.updateFilters { copy(tag = it) } },
            filterYear = filters.year,
            onFilterYearChange = { viewModel.updateFilters { copy(year = it) } },
            availableYears = availableYears,
            groups = viewModel.groups,
            onClearFilters = { viewModel.clearFilters() },
            onExport = {
                val dialog = FileDialog(null as Frame?, strExportDialogTitle, FileDialog.SAVE)
                dialog.file = "transacoes_${LocalDate.now()}.csv"
                dialog.isVisible = true
                val dir = dialog.directory
                val name = dialog.file
                dialog.dispose()
                if (dir != null && name != null) {
                    val safeName = if (name.endsWith(".csv")) name else "$name.csv"
                    val target = File(dir, safeName)
                    scope.launch(Dispatchers.IO) {
                        viewModel.exportToCsv(displayedTransactions, target)
                    }
                }
            },
        )
    }
}

@Composable
private fun MonthSection(
    month: Month,
    transactions: List<Transaction>,
    onTransactionClick: (Transaction) -> Unit,
    onTransactionDelete: (Transaction) -> Unit,
) {
    MonthHeader(modifier = Modifier.zIndex(1f), month = month)
    Column(
        modifier = Modifier
            .padding(start = 80.dp, end = 30.dp)
            .zIndex(2f)
            .clip(RoundedCornerShape(topEnd = 0.dp, bottomStart = 0.dp))
            .background(
                MaterialTheme.colors.background.copy(0.6f),
                RoundedCornerShape(topEnd = 0.dp, bottomStart = 0.dp)
            )
            .border(
                0.5.dp,
                MaterialTheme.colors.onSurface,
                RoundedCornerShape(topEnd = 0.dp, bottomStart = 0.dp)
            )
    ) {
        Spacer(Modifier.height(20.dp))
        transactions.forEach { transaction ->
            TransactionRow(
                transaction = transaction,
                onDelete = { onTransactionDelete(transaction) },
                onClick = { onTransactionClick(transaction) }
            )
        }
        Spacer(Modifier.height(20.dp))
    }

    val positive = transactions.filter { it.balance >= 0 }.sumOf { it.balance }
    val negative = transactions.filter { it.balance < 0 }.sumOf { it.balance } * -1
    TotalFooter(modifier = Modifier.zIndex(1f), incomeBalance = positive, outcomeBalance = negative)
    Spacer(Modifier.height(30.dp))
}


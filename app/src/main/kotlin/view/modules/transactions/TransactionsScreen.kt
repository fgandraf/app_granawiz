package view.modules.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.bold.ListBullets
import com.adamglin.phosphoricons.regular.*
import com.felipegandra.generated.resources.*
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.entity.account.CreditCardAccount
import domain.enums.TransactionType
import domain.structs.PageAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import view.modules.Screen
import view.modules.transactionForm.TransactionForm
import view.modules.transactions.component.DropDownAddTransaction
import view.modules.transactions.component.MonthHeader
import view.modules.transactions.component.TotalFooter
import view.modules.transactions.component.TransactionRow
import view.shared.*
import viewModel.TransactionViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import utils.computeBillingYearMonth
import java.time.LocalDate
import java.time.YearMonth


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
    var transactionType by remember { mutableStateOf<TransactionType?>(null) }
    var isTransfer by remember { mutableStateOf(false) }

    val strTransactionEditIncome = stringResource(Res.string.transaction_edit_income)
    val strTransactionEditExpense = stringResource(Res.string.transaction_edit_expense)
    val strTransactionNewIncome = stringResource(Res.string.transaction_new_income)
    val strTransactionNewExpense = stringResource(Res.string.transaction_new_expense)
    val strTransactionNewTransfer = stringResource(Res.string.transaction_new_transfer)

    LaunchedEffect(account) {
        addresses = initialAddress
        showEditTransaction = false
        selectedTransaction = null
        backIcon = false
        showTransactionsList = true
        isTransfer = false
        viewModel.clearFilters()
    }

    val transactionsState by viewModel.transactions.collectAsState()
    val filters by viewModel.filters.collectAsState()
    val groups by viewModel.groups.collectAsState()
    val allAccounts = remember(groups) { groups.flatMap { it.accounts } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 15.dp, bottom = 15.dp)
            .border(1.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colors.surface)
    ) {

        DefaultScreenHeader(
            addresses = addresses,
            backEnabled = backIcon,
            onBackClick = {
                addresses = initialAddress
                selectedTransaction = null
                showEditTransaction = false
                backIcon = false
                showTransactionsList = true
            },
            subtitle = account?.description?.takeIf { it.isNotEmpty() },
            trailingContent = if (showTransactionsList && transactionsState.isNotEmpty()) {
                { SearchField(value = filters.searchQuery, onValueChange = { viewModel.updateFilters { copy(searchQuery = it) } }) }
            } else null
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
                    isTransfer = false
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
                    isTransfer = false
                    showTransactionsList = false
                    showEditTransaction = true
                    addresses = addresses + PageAddress(
                        iconVector = PhosphorIcons.Regular.MinusSquare,
                        iconSize = DpSize(21.dp, 18.dp),
                        name = strTransactionNewExpense
                    )
                    backIcon = true
                },
                onAddTransfer = {
                    transactionType = null
                    isTransfer = true
                    showTransactionsList = false
                    showEditTransaction = true
                    addresses = addresses + PageAddress(
                        iconVector = PhosphorIcons.Regular.Swap,
                        iconSize = DpSize(21.dp, 18.dp),
                        name = strTransactionNewTransfer
                    )
                    backIcon = true
                },
                onImport = { onScreenChange(Screen.ImportStatement(account = viewModel.selectedAccount)) },
                onDismissAdd = {
                    selectedTransaction = null
                    showEditTransaction = false
                    showTransactionsList = true
                    isTransfer = false
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
                isTransfer = isTransfer,
                allAccounts = allAccounts,
                onDismiss = { saved ->
                    backIcon = false
                    showEditTransaction = false
                    showTransactionsList = true
                    isTransfer = false
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
private fun Body(
    showAddButton: Boolean,
    viewModel: TransactionViewModel,
    account: BankAccount?,
    onTransactionClick: (Transaction) -> Unit,
    onTransactionDeleted: () -> Unit,
    onAddGain: () -> Unit,
    onAddExpense: () -> Unit,
    onAddTransfer: () -> Unit,
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
                    val currentYearMonth = YearMonth.now()
                    val creditCard = account as? CreditCardAccount
                    val monthTransactions = displayedTransactions
                        .groupBy { tx ->
                            if (creditCard != null) {
                                tx.billingYearMonth?.let { YearMonth.parse(it) }
                                    ?: computeBillingYearMonth(tx.date, tx.originalDueDate, tx.scheduleId, creditCard.closingDay)
                            } else {
                                YearMonth.from(tx.date)
                            }
                        }
                        .mapValues { (_, txs) -> txs.sortedByDescending { it.date } }
                    val sortedMonths = monthTransactions.keys
                        .sortedWith(compareByDescending<YearMonth> { it == currentYearMonth }.thenByDescending { it })
                    item { Spacer(modifier = Modifier.height(30.dp)) }
                    sortedMonths.forEach { yearMonth ->
                        val transactions = monthTransactions[yearMonth] ?: return@forEach
                        item {
                            MonthSection(
                                yearMonth = yearMonth,
                                transactions = transactions,
                                onTransactionClick = onTransactionClick,
                                onTransactionDelete = { tx ->
                                    viewModel.deleteTransaction(tx)
                                    onTransactionDeleted()
                                },
                                onTransactionFlag = { tx -> viewModel.flagTransaction(tx) },
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
                                onClickTransfer = {
                                    showAddDropDown = false
                                    onAddTransfer()
                                },
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
    yearMonth: YearMonth,
    transactions: List<Transaction>,
    onTransactionClick: (Transaction) -> Unit,
    onTransactionDelete: (Transaction) -> Unit,
    onTransactionFlag: (Transaction) -> Unit,
) {
    val regular = transactions.filter { it.installment == "1/1" }
    val installments = transactions.filter { it.installment != "1/1" }

    MonthHeader(modifier = Modifier.zIndex(1f), yearMonth = yearMonth)
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
        regular.forEach { transaction ->
            TransactionRow(
                transaction = transaction,
                onDelete = { onTransactionDelete(transaction) },
                onFlag = { onTransactionFlag(transaction) },
                onClick = { onTransactionClick(transaction) }
            )
        }
        if (installments.isNotEmpty()) {
            if (regular.isNotEmpty()) Divider(color = MaterialTheme.colors.onSurface)
            TextSmall(
                text = stringResource(Res.string.transactions_installment_group),
                weight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(start = 10.dp, top = 8.dp, bottom = 4.dp)
            )
            installments.forEach { transaction ->
                TransactionRow(
                    transaction = transaction,
                    onDelete = { onTransactionDelete(transaction) },
                    onFlag = { onTransactionFlag(transaction) },
                    onClick = { onTransactionClick(transaction) }
                )
            }
        }
        Spacer(Modifier.height(20.dp))
    }

    val positive = transactions.filter { it.balance >= 0 }.sumOf { it.balance }
    val negative = transactions.filter { it.balance < 0 }.sumOf { it.balance } * -1
    TotalFooter(modifier = Modifier.zIndex(1f), incomeBalance = positive, outcomeBalance = negative)
    Spacer(Modifier.height(30.dp))
}


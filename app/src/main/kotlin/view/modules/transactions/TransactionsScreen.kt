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
import androidx.compose.ui.zIndex
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.bold.ListBullets
import com.adamglin.phosphoricons.light.Plus
import com.adamglin.phosphoricons.regular.*
import com.felipegandra.generated.resources.*
import domain.entity.Category
import domain.entity.Subcategory
import domain.entity.Transaction
import domain.entity.account.BankAccount
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
import view.theme.ButtonPurple
import viewModel.TransactionViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.time.LocalDate
import domain.entity.Tag as TagEntity


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

    var searchQuery by remember { mutableStateOf("") }
    var filterAccount by remember { mutableStateOf<BankAccount?>(null) }

    var filterCategoryItem by remember { mutableStateOf<Pair<Category, Subcategory?>?>(null) }

    var filterTag by remember { mutableStateOf<TagEntity?>(null) }

    var filterType by remember { mutableStateOf<TransactionType?>(null) }

    var filterYear by remember { mutableStateOf<Int?>(LocalDate.now().year) }

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
        searchQuery = ""
        filterAccount = null
        filterCategoryItem = null
        filterTag = null
        filterType = null
        filterYear = LocalDate.now().year
    }

    val transactionsState by viewModel.transactions.collectAsState()
    val scope = rememberCoroutineScope()

    val availableYears = remember(transactionsState) {
        transactionsState.map { it.date.year }.distinct().sortedDescending()
    }

    val displayedTransactions = remember(transactionsState, searchQuery, filterAccount, filterCategoryItem, filterTag, filterType, filterYear) {
        transactionsState.filter { transaction ->
            val matchesSearch = searchQuery.isEmpty() ||
                    transaction.party.name.contains(searchQuery, ignoreCase = true) ||
                    transaction.description.contains(searchQuery, ignoreCase = true) ||
                    transaction.category.name.contains(searchQuery, ignoreCase = true) ||
                    transaction.subcategory?.name?.contains(searchQuery, ignoreCase = true) == true ||
                    transaction.tags?.any { it.name.contains(searchQuery, ignoreCase = true) } == true ||
                    transaction.balance.toString().contains(searchQuery, ignoreCase = true)
            val matchesAccount = filterAccount == null || transaction.account.id == filterAccount!!.id
            val matchesCategory = filterCategoryItem == null ||
                    (filterCategoryItem!!.second == null && transaction.category.id == filterCategoryItem!!.first.id) ||
                    (filterCategoryItem!!.second != null && transaction.subcategory?.id == filterCategoryItem!!.second!!.id)
            val matchesTag = filterTag == null || transaction.tags?.any { it.id == filterTag!!.id } == true
            val matchesType = filterType == null || transaction.type == filterType
            val matchesYear = filterYear == null || transaction.date.year == filterYear
            matchesSearch && matchesAccount && matchesCategory && matchesTag && matchesType && matchesYear
        }
    }

    var transactionType by remember { mutableStateOf(selectedTransaction?.type) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 15.dp, bottom = 15.dp)
            .border(1.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colors.surface)
    ) {

        // ********** HEADER **********
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
                        addresses = initialAddress
                        selectedTransaction = null
                        showEditTransaction = false
                        backIcon = false
                        showTransactionsList = true
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

                if (showTransactionsList && transactionsState.isNotEmpty())
                    SearchField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it }
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

        // ********** BODY **********
        if (showTransactionsList) {
            val listState = rememberLazyListState()
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
                                                viewModel = viewModel,
                                                transaction = transaction,
                                                onDeleted = onSidebarReload,
                                                onClick = {
                                                    addresses = addresses + PageAddress(
                                                        iconVector = PhosphorIcons.Regular.Pencil,
                                                        iconSize = DpSize(21.dp, 18.dp),
                                                        name = if (transaction.type == TransactionType.GAIN) strTransactionEditIncome else strTransactionEditExpense
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

                                    val positive =
                                        displayedTransactions.filter { it.date.month == month && it.balance >= 0 }
                                            .sumOf { it.balance }
                                    val negative =
                                        displayedTransactions.filter { it.date.month == month && it.balance < 0 }
                                            .sumOf { it.balance } * -1
                                    TotalFooter(
                                        modifier = Modifier.zIndex(1f),
                                        incomeBalance = positive,
                                        outcomeBalance = negative
                                    )
                                    Spacer(Modifier.height(30.dp))
                                }

                            }
                            item { Spacer(Modifier.height(50.dp)) }

                        }


                        if (showAddButton)
                            AddTransactionButton(
                                onClickGain = {
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
                                onClickExpense = {
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
                                onClickImport = {
                                    onScreenChange(Screen.ImportStatement(account = viewModel.selectedAccount))
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

                Box(modifier = Modifier.fillMaxHeight()) {
                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(listState),
                        modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd)
                    )
                    Row(
                        modifier = Modifier.fillMaxHeight().width(45.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
                                .background(MaterialTheme.colors.surface)
                                .padding(vertical = 5.dp)
                        ) {
                            if (showTransactionsList && transactionsState.isNotEmpty()) {
                                FilterTransactionBar(
                                    items = viewModel.transactions,
                                    searchQuery = searchQuery,
                                    currentAccountView = account,
                                    filterAccount = filterAccount,
                                    onFilterAccountChange = { filterAccount = it },
                                    filterType = filterType,
                                    onFilterTypeChange = { filterType = it },
                                    filterCategoryItem = filterCategoryItem,
                                    onFilterCategoryItemChange = { filterCategoryItem = it },
                                    filterTag = filterTag,
                                    onFilterTagChange = { filterTag = it },
                                    filterYear = filterYear,
                                    onFilterYearChange = { filterYear = it },
                                    availableYears = availableYears,
                                    groups = viewModel.groups,
                                    onClearFilters = {
                                        filterAccount = null
                                        filterType = null
                                        filterCategoryItem = null
                                        filterTag = null
                                        filterYear = LocalDate.now().year
                                    },
                                    onExport = {
                                        val dialog =
                                            FileDialog(null as Frame?, "Exportar transações para CSV", FileDialog.SAVE)
                                        dialog.file = "transacoes_${LocalDate.now()}.csv"
                                        dialog.isVisible = true
                                        val dir = dialog.directory
                                        val name = dialog.file
                                        dialog.dispose()
                                        if (dir != null && name != null) {
                                            val safeName = if (name.endsWith(".csv")) name else "$name.csv"
                                            val target = File(dir, safeName)
                                            scope.launch(Dispatchers.IO) {
                                                viewModel.exportToCsv(
                                                    displayedTransactions,
                                                    target
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

            }
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
fun AddTransactionButton(
    onClickGain: () -> Unit,
    onClickExpense: () -> Unit,
    onClickImport: () -> Unit,
    onDismiss: () -> Unit,
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
                    onClickImport = {
                        showAddTransactionDropDownMenu = false
                        onClickImport()
                    },
                    onDismissRequest = {
                        onDismiss()
                        showAddTransactionDropDownMenu = false
                    }
                )
            }
        }
    }
}
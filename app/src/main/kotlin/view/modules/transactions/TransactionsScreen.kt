package view.modules.transactions

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
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
import com.adamglin.phosphoricons.light.Bank
import com.adamglin.phosphoricons.light.CaretDown
import com.adamglin.phosphoricons.light.Plus
import com.adamglin.phosphoricons.light.Shapes
import com.adamglin.phosphoricons.regular.*
import core.entity.Category
import core.entity.Subcategory
import core.entity.Transaction
import core.entity.account.BankAccount
import core.enums.TransactionType
import core.structs.PageAddress
import view.modules.transactionForm.TransactionForm
import view.modules.transactions.component.DropDownAddTransaction
import view.modules.transactions.component.MonthHeader
import view.modules.transactions.component.TotalFooter
import view.modules.transactions.component.TransactionRow
import view.shared.*
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
                    name = "Todas as transações",
                    rootPath = true
                )
            )


    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }
    var showEditTransaction by remember { mutableStateOf(false) }
    var backIcon by remember { mutableStateOf(false) }
    var showTransactionsList by remember { mutableStateOf(true) }

    var searchQuery by remember { mutableStateOf("") }
    var filterAccount by remember { mutableStateOf<BankAccount?>(null) }
    var showAccountDropdown by remember { mutableStateOf(false) }
    var filterCategoryItem by remember { mutableStateOf<Pair<Category, Subcategory?>?>(null) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    var addresses by remember { mutableStateOf(emptyList<PageAddress>()) }
    LaunchedEffect(account) {
        addresses = initialAddress
        showEditTransaction = false
        selectedTransaction = null
        backIcon = false
        showTransactionsList = true
        searchQuery = ""
        filterAccount = null
        filterCategoryItem = null
    }

    var transactionType by remember { mutableStateOf(selectedTransaction?.type) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        // ********** HEADER **********
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            // address
            Column {
                Row(Modifier.padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
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
                TextNormal(text = account?.description ?: "", align = TextAlign.Start)
            }

            if (showTransactionsList) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (account == null) {
                        val allAccounts = viewModel.groups.value.flatMap { it.accounts }
                        val filterLabel = filterAccount?.name ?: "Todos os bancos"
                        Box {
                            Box(
                                modifier = Modifier
                                    .height(35.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(8.dp))
                                    .background(Color.Transparent)
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .clickable { showAccountDropdown = true }
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(
                                        imageVector = PhosphorIcons.Light.Bank,
                                        contentDescription = "",
                                        tint = MaterialTheme.colors.secondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    TextMedium(text = filterLabel)
                                    Icon(
                                        imageVector = PhosphorIcons.Light.CaretDown,
                                        contentDescription = "",
                                        tint = MaterialTheme.colors.secondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = showAccountDropdown,
                                onDismissRequest = { showAccountDropdown = false }
                            ) {
                                DropdownMenuItem(onClick = {
                                    filterAccount = null
                                    showAccountDropdown = false
                                }) {
                                    TextNormal(text = "Todos os bancos")
                                }
                                allAccounts.forEach { acc: BankAccount ->
                                    DropdownMenuItem(onClick = {
                                        filterAccount = acc
                                        showAccountDropdown = false
                                    }) {
                                        TextNormal(text = acc.name)
                                    }
                                }
                            }
                        }
                    }

                    val preFilteredTransactions = viewModel.transactions.value.filter { t ->
                        val ms = searchQuery.isEmpty() ||
                                t.party.name.contains(searchQuery, ignoreCase = true) ||
                                t.description.contains(searchQuery, ignoreCase = true) ||
                                t.category.name.contains(searchQuery, ignoreCase = true) ||
                                t.subcategory?.name?.contains(searchQuery, ignoreCase = true) == true ||
                                t.tags?.any { it.name.contains(searchQuery, ignoreCase = true) } == true
                        val ma = filterAccount == null || t.account.id == filterAccount!!.id
                        ms && ma
                    }
                    val availableCategoriesMap = preFilteredTransactions.groupBy { it.category }
                    val categoryFilterLabel = filterCategoryItem?.let { (cat, sub) ->
                        if (sub != null) "${cat.name}: ${sub.name}" else cat.name
                    } ?: "Todas as categorias"

                    Box {
                        Box(
                            modifier = Modifier
                                .height(35.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(8.dp))
                                .background(Color.Transparent)
                                .pointerHoverIcon(PointerIcon.Hand)
                                .clickable { showCategoryDropdown = true }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(
                                    imageVector = PhosphorIcons.Light.Shapes,
                                    contentDescription = "",
                                    tint = MaterialTheme.colors.secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                TextMedium(text = categoryFilterLabel)
                                Icon(
                                    imageVector = PhosphorIcons.Light.CaretDown,
                                    contentDescription = "",
                                    tint = MaterialTheme.colors.secondary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false }
                        ) {
                            DropdownMenuItem(onClick = {
                                filterCategoryItem = null
                                showCategoryDropdown = false
                            }) {
                                TextNormal(text = "Todas as categorias")
                            }
                            availableCategoriesMap.forEach { (cat, transactions) ->
                                DropdownMenuItem(onClick = {
                                    filterCategoryItem = Pair(cat, null)
                                    showCategoryDropdown = false
                                }) {
                                    TextNormal(text = cat.name)
                                }
                                transactions.mapNotNull { it.subcategory }.distinctBy { it.id }.forEach { sub ->
                                    DropdownMenuItem(onClick = {
                                        filterCategoryItem = Pair(cat, sub)
                                        showCategoryDropdown = false
                                    }) {
                                        TextNormal(modifier = Modifier.padding(start = 16.dp), text = sub.name)
                                    }
                                }
                            }
                        }
                    }
                    SearchBar(
                        value = searchQuery,
                        onValueChange = { searchQuery = it }
                    )
                }
            }
        }

        // ********** BODY **********
        if (showTransactionsList) {
            val displayedTransactions = viewModel.transactions.value.filter { transaction ->
                val matchesSearch = searchQuery.isEmpty() ||
                        transaction.party.name.contains(searchQuery, ignoreCase = true) ||
                        transaction.description.contains(searchQuery, ignoreCase = true) ||
                        transaction.category.name.contains(searchQuery, ignoreCase = true) ||
                        transaction.subcategory?.name?.contains(searchQuery, ignoreCase = true) == true ||
                        transaction.tags?.any { it.name.contains(searchQuery, ignoreCase = true) } == true
                val matchesAccount = filterAccount == null || transaction.account.id == filterAccount!!.id
                val matchesCategory = filterCategoryItem == null ||
                        (filterCategoryItem!!.second == null && transaction.category.id == filterCategoryItem!!.first.id) ||
                        (filterCategoryItem!!.second != null && transaction.subcategory?.id == filterCategoryItem!!.second!!.id)
                matchesSearch && matchesAccount && matchesCategory
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (displayedTransactions.isEmpty())
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        TextH2(text = "Nenhuma transação encontrada.")
                    }

                val listState = rememberLazyListState()

                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    val monthTransactions = displayedTransactions.groupBy { it.date.month }

                    item { Spacer(modifier = Modifier.height(30.dp)) }
                    monthTransactions.forEach { (month, transactions) ->

                        item {
                            MonthHeader(modifier = Modifier.zIndex(1f), month = month)
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 90.dp)
                                    .zIndex(2f)
                                    .clip(RoundedCornerShape(topEnd = 0.dp, bottomStart = 0.dp))
                                    .background(
                                        MaterialTheme.colors.surface,
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
                                        onClick = {
                                            addresses = addresses + PageAddress(
                                                iconVector = PhosphorIcons.Regular.Pencil,
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
                                iconVector = PhosphorIcons.Regular.PlusSquare,
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
                                iconVector = PhosphorIcons.Regular.MinusSquare,
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
                onDismiss = { updated, account ->
                    backIcon = false
                    showEditTransaction = false
                    showTransactionsList = true
                    addresses = initialAddress

                    if (updated) {
                        viewModel.getTransactions()
                        val calculated = viewModel.transactions.value.sumOf { it.balance }
                        viewModel.updateBalance(account.id, calculated)
                    }

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
                    onDismissRequest = {
                        onDismiss()
                        showAddTransactionDropDownMenu = false
                    }
                )
            }
        }
    }
}
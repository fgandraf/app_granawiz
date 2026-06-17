package viewModel

import application.account.AccountHandler
import application.group.GroupHandler
import application.transaction.TransactionHandler
import domain.entity.Category
import domain.entity.Group
import domain.entity.Subcategory
import domain.entity.Tag
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.TransactionType
import infrastructure.di.ApplicationContainer
import kotlinx.coroutines.flow.*
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent
import java.io.File
import java.time.LocalDate

data class FilterState(
    val searchQuery: String = "",
    val account: BankAccount? = null,
    val categoryItem: Pair<Category, Subcategory?>? = null,
    val tag: Tag? = null,
    val type: TransactionType? = null,
    val year: Int? = LocalDate.now().year,
)

class TransactionViewModel(
    account: BankAccount? = null,
    private val accountHandler: AccountHandler = ApplicationContainer.accountHandler,
    private val transactionHandler: TransactionHandler = ApplicationContainer.transactionHandler,
    private val groupHandler: GroupHandler = ApplicationContainer.groupHandler,
) {

    var selectedAccount = account
    fun selectAccount(account: BankAccount) {
        selectedAccount = account
    }

    private val _transactions = MutableStateFlow(emptyList<Transaction>())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    fun getTransactions() {
        runCatching {
            _transactions.value = transactionHandler.fetchTransactions(account = selectedAccount)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    private val _groups = MutableStateFlow(emptyList<Group>())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()

    fun getGroups() {
        runCatching {
            _groups.value = groupHandler.fetchGroups()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    private val _filters = MutableStateFlow(FilterState())
    val filters: StateFlow<FilterState> = _filters.asStateFlow()

    fun updateFilters(update: FilterState.() -> FilterState) {
        _filters.update { it.update() }
    }

    fun clearFilters() {
        _filters.value = FilterState()
    }

    val availableYears: Flow<List<Int>> = _transactions.map { list ->
        list.map { it.date.year }.distinct().sortedDescending()
    }

    val displayedTransactions: Flow<List<Transaction>> = combine(_transactions, _filters) { list, f ->
        list.filter { transaction ->
            val matchesSearch = f.searchQuery.isEmpty() ||
                    transaction.party.name.contains(f.searchQuery, ignoreCase = true) ||
                    transaction.description.contains(f.searchQuery, ignoreCase = true) ||
                    transaction.category.name.contains(f.searchQuery, ignoreCase = true) ||
                    transaction.subcategory?.name?.contains(f.searchQuery, ignoreCase = true) == true ||
                    transaction.tags?.any { it.name.contains(f.searchQuery, ignoreCase = true) } == true ||
                    transaction.balance.toString().contains(f.searchQuery, ignoreCase = true)
            val matchesAccount = f.account == null || transaction.account.id == f.account.id
            val matchesCategory = f.categoryItem == null ||
                    (f.categoryItem.second == null && transaction.category.id == f.categoryItem.first.id) ||
                    (f.categoryItem.second != null && transaction.subcategory?.id == f.categoryItem.second!!.id)
            val matchesTag = f.tag == null || transaction.tags?.any { it.id == f.tag.id } == true
            val matchesType = f.type == null || transaction.type == f.type
            val matchesYear = f.year == null || transaction.date.year == f.year
            matchesSearch && matchesAccount && matchesCategory && matchesTag && matchesType && matchesYear
        }
    }

    init {
        getTransactions()
        getGroups()
    }

    fun deleteTransaction(transaction: Transaction) {
        runCatching {
            transactionHandler.deleteTransaction(transaction)
            getTransactions()
            val accountTransactions = transactionHandler.fetchTransactions(account = transaction.account)
            accountHandler.updateBalance(transaction.account.id, accountTransactions.sumOf { it.balance })
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun exportToCsv(transactions: List<Transaction>, file: File) {
        runCatching {
            transactionHandler.exportTransactionsToCsv(transactions, file)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun updateBalance(accountId: Long, amount: Double) {
        runCatching {
            accountHandler.updateBalance(accountId, amount)
            getTransactions()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }
}

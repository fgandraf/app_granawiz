package viewModel

import domain.entity.Group
import domain.entity.Transaction
import domain.entity.account.BankAccount
import application.account.AccountHandler
import infrastructure.di.ApplicationContainer
import application.group.GroupHandler
import kotlinx.coroutines.flow.MutableStateFlow
import application.transaction.TransactionHandler
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent
import java.io.File

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

    var transactions = MutableStateFlow(emptyList<Transaction>())
    fun getTransactions() {
        runCatching {
            transactions.value = transactionHandler.fetchTransactions(account = selectedAccount)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    var groups = MutableStateFlow(emptyList<Group>())
    fun getGroups() {
        runCatching {
            groups.value = groupHandler.fetchGroups()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    init {
        getTransactions()
        if (account == null) getGroups()
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

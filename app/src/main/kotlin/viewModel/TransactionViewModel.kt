package viewModel

import core.entity.Group
import core.entity.Transaction
import core.entity.account.BankAccount
import domain.account.AccountHandler
import domain.group.GroupHandler
import kotlinx.coroutines.flow.MutableStateFlow
import domain.transaction.TransactionHandler

class TransactionViewModel(
    account: BankAccount? = null,
    private val accountHandler: AccountHandler = AccountHandler(),
    private val transactionHandler: TransactionHandler = TransactionHandler(),
    private val groupHandler: GroupHandler = GroupHandler(),
) {

    var selectedAccount = account
    fun selectAccount(account: BankAccount) {
        selectedAccount = account
    }

    var transactions = MutableStateFlow(emptyList<Transaction>())
    fun getTransactions() {
        transactions.value = transactionHandler.fetchTransactions(account = selectedAccount)
    }

    var groups = MutableStateFlow(emptyList<Group>())
    fun getGroups() {
        groups.value = groupHandler.fetchGroups()
    }

    init {
        getTransactions()
        if (account == null) getGroups()
    }

    fun deleteTransaction(transaction: Transaction) {
        transactionHandler.deleteTransaction(transaction)
        getTransactions()
    }

    fun updateBalance(accountId: Long, amount: Double) {
        accountHandler.updateBalance(accountId, amount)
        getTransactions()
    }

}
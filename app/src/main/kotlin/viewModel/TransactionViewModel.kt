package viewModel

import core.entity.Transaction
import core.entity.account.BankAccount
import domain.account.AccountHandler
import kotlinx.coroutines.flow.MutableStateFlow
import domain.transaction.TransactionHandler

class TransactionViewModel(
    account: BankAccount? = null,
    private val accountHandler: AccountHandler = AccountHandler(),
    private val usecases: TransactionHandler = TransactionHandler()
) {

    var selectedAccount = account
    fun selectAccount(account: BankAccount){ selectedAccount = account }


    var transactions = MutableStateFlow(emptyList<Transaction>())
    fun getTransactions() {
        transactions.value = usecases.fetchTransactions(account = selectedAccount) }

    init {
        getTransactions()
    }

    fun deleteTransaction(transaction: Transaction){
        usecases.deleteTransaction(transaction)
    }

    fun updateBalance(accountId: Long, amount: Double) {
        accountHandler.updateBalance(accountId, amount)
    }


}
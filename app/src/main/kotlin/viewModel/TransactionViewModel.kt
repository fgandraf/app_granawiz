package viewModel

import core.entity.Transaction
import core.entity.account.BankAccount
import kotlinx.coroutines.flow.MutableStateFlow
import service.AccountService
import service.TransactionService

class TransactionViewModel(account: BankAccount? = null) {

    private val service: TransactionService = TransactionService()


    var selectedAccount = account
    fun selectAccount(account: BankAccount){ selectedAccount = account }


    var transactions = MutableStateFlow(emptyList<Transaction>())
    fun getTransactions() {
        transactions.value = service.loadTransactions(account = selectedAccount) }

    init {
        getTransactions()
    }

    fun deleteTransaction(transaction: Transaction){
        service.deleteTransaction(transaction)
    }

    fun updateBalance(account: BankAccount, amount: Double) {

        val accountViewModel = AccountFormViewModel()
        accountViewModel.initializeFromAccount(account)
        accountViewModel.balance = amount
        val accountService = AccountService(accountViewModel)

        accountService.saveAccount(account.type, account)
    }


}
package viewModel

import core.entity.Transaction
import core.entity.account.BankAccount
import kotlinx.coroutines.flow.MutableStateFlow
import domain.transaction.TransactionHandler
import domain.account.usecases.SaveAccountUseCase

class TransactionViewModel(
    account: BankAccount? = null,
    private val saveAccountUseCase: SaveAccountUseCase = SaveAccountUseCase(),
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

    fun updateBalance(account: BankAccount, amount: Double) {

        val accountViewModel = AccountFormViewModel()
        accountViewModel.initializeFromAccount(account)
        accountViewModel.balance = amount

        saveAccountUseCase.execute(type =  account.type, account =  account)

    }


}
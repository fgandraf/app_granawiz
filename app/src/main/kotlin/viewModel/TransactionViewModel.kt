package viewModel

import androidx.compose.runtime.derivedStateOf
import core.entity.account.BankAccount
import service.TransactionService

class TransactionViewModel(account: BankAccount? = null) {

    val service: TransactionService = TransactionService()

    var selectedAccount = account
    fun selectAccount(account: BankAccount){ selectedAccount = account }

    var transactions = derivedStateOf { service.transactions }

    init { service.loadTransactions(selectedAccount) }
}
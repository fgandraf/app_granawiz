package domain.transaction

import core.entity.Transaction
import core.entity.account.BankAccount
import domain.transaction.usecases.DeleteTransactionUseCase
import domain.transaction.usecases.FetchTransactionsUseCase
import domain.transaction.usecases.SaveTransactionUseCase

class TransactionHandler {

    private val saveTransactionUseCase = SaveTransactionUseCase()
    private val deleteTransactionUseCase = DeleteTransactionUseCase()
    private val fetchTransactionsUseCase = FetchTransactionsUseCase()

    fun saveTransaction(transaction: Transaction) = saveTransactionUseCase.execute(transaction)
    fun deleteTransaction(transaction: Transaction) = deleteTransactionUseCase.execute(transaction)
    fun fetchTransactions(account: BankAccount? = null) = fetchTransactionsUseCase.execute(account)

}

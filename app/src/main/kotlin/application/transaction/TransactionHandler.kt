package application.transaction

import domain.entity.Transaction
import domain.entity.account.BankAccount
import application.transaction.usecases.DeleteTransactionUseCase
import application.transaction.usecases.FetchTransactionsUseCase
import application.transaction.usecases.SaveTransactionUseCase

class TransactionHandler {

    private val saveTransactionUseCase = SaveTransactionUseCase()
    private val deleteTransactionUseCase = DeleteTransactionUseCase()
    private val fetchTransactionsUseCase = FetchTransactionsUseCase()

    fun saveTransaction(transaction: Transaction) = saveTransactionUseCase.execute(transaction)
    fun deleteTransaction(transaction: Transaction) = deleteTransactionUseCase.execute(transaction)
    fun fetchTransactions(account: BankAccount? = null) = fetchTransactionsUseCase.execute(account)

}

package domain.transaction

import core.entity.Transaction
import core.entity.account.BankAccount
import domain.transaction.usecases.AddTransactionUseCase
import domain.transaction.usecases.DeleteTransactionUseCase
import domain.transaction.usecases.FetchTransactionsUseCase
import domain.transaction.usecases.UpdateTransactionUseCase

class TransactionHandler(
    private val addTransactionUseCase: AddTransactionUseCase = AddTransactionUseCase(),
    private val deleteTransactionUseCase: DeleteTransactionUseCase = DeleteTransactionUseCase(),
    private val fetchTransactionsUseCase: FetchTransactionsUseCase = FetchTransactionsUseCase(),
    private val updateTransactionUseCase: UpdateTransactionUseCase = UpdateTransactionUseCase()
) {

    fun addTransaction(transaction: Transaction) = addTransactionUseCase.execute(transaction)
    fun deleteTransaction(transaction: Transaction) = deleteTransactionUseCase.execute(transaction)
    fun fetchTransactions(account: BankAccount? = null) = fetchTransactionsUseCase.execute(account)
    fun updateTransaction(transaction: Transaction) = updateTransactionUseCase.execute(transaction)

}
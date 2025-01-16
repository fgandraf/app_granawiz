package domain.transaction

import core.entity.Transaction
import core.entity.account.BankAccount
import domain.transaction.usecases.SaveTransactionUseCase
import domain.transaction.usecases.DeleteTransactionUseCase
import domain.transaction.usecases.FetchTransactionsUseCase
import viewModel.TransactionFormViewModel

class TransactionHandler {

    private val saveTransactionUseCase = SaveTransactionUseCase()
    private val deleteTransactionUseCase = DeleteTransactionUseCase()
    private val fetchTransactionsUseCase = FetchTransactionsUseCase()


    fun saveTransaction(viewModel: TransactionFormViewModel) = saveTransactionUseCase.execute(viewModel)
    fun deleteTransaction(transaction: Transaction) = deleteTransactionUseCase.execute(transaction)
    fun fetchTransactions(account: BankAccount? = null) = fetchTransactionsUseCase.execute(account)

}
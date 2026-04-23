package application.transaction

import domain.entity.Transaction
import domain.entity.account.BankAccount
import application.transaction.usecases.DeleteTransactionUseCase
import application.transaction.usecases.ExportTransactionsToExcelUseCase
import application.transaction.usecases.FetchTransactionsUseCase
import application.transaction.usecases.SaveTransactionUseCase
import java.io.File

class TransactionHandler {

    private val saveTransactionUseCase = SaveTransactionUseCase()
    private val deleteTransactionUseCase = DeleteTransactionUseCase()
    private val fetchTransactionsUseCase = FetchTransactionsUseCase()
    private val exportTransactionsToExcelUseCase = ExportTransactionsToExcelUseCase()

    fun saveTransaction(transaction: Transaction) = saveTransactionUseCase.execute(transaction)
    fun deleteTransaction(transaction: Transaction) = deleteTransactionUseCase.execute(transaction)
    fun fetchTransactions(account: BankAccount? = null) = fetchTransactionsUseCase.execute(account)
    fun exportTransactionsToExcel(transactions: List<Transaction>, file: File) =
        exportTransactionsToExcelUseCase.execute(transactions, file)

}

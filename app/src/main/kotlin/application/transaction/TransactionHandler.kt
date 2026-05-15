package application.transaction

import domain.contracts.ITransactionRepository
import domain.entity.Transaction
import domain.entity.account.BankAccount
import application.transaction.usecases.ExportTransactionsToCsvUseCase
import application.transaction.usecases.FetchTransactionsUseCase
import application.transaction.usecases.SaveTransactionUseCase
import java.io.File

class TransactionHandler(
    private val transactionRepository: ITransactionRepository,
    private val saveTransactionUseCase: SaveTransactionUseCase,
    private val fetchTransactionsUseCase: FetchTransactionsUseCase,
    private val exportTransactionsToCsvUseCase: ExportTransactionsToCsvUseCase,
) {

    fun saveTransaction(transaction: Transaction) = saveTransactionUseCase.execute(transaction)
    fun deleteTransaction(transaction: Transaction) = transactionRepository.delete(transaction)
    fun fetchTransactions(account: BankAccount? = null) = fetchTransactionsUseCase.execute(account)
    fun exportTransactionsToCsv(transactions: List<Transaction>, file: File) =
        exportTransactionsToCsvUseCase.execute(transactions, file)
}

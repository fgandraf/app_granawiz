package application.transaction

import domain.contracts.ITransactionRepository
import domain.entity.Transaction
import domain.entity.account.BankAccount
import application.transaction.usecases.ExportTransactionsToCsvUseCase
import application.transaction.usecases.FetchTransactionsUseCase
import application.transaction.usecases.SaveTransactionUseCase
import application.transaction.usecases.SaveTransferUseCase
import java.io.File
import java.time.LocalDateTime

class TransactionHandler(
    private val transactionRepository: ITransactionRepository,
    private val saveTransactionUseCase: SaveTransactionUseCase,
    private val saveTransferUseCase: SaveTransferUseCase,
    private val fetchTransactionsUseCase: FetchTransactionsUseCase,
    private val exportTransactionsToCsvUseCase: ExportTransactionsToCsvUseCase,
) {

    fun saveTransaction(transaction: Transaction) = saveTransactionUseCase.execute(transaction)
    fun saveTransfer(source: BankAccount, destination: BankAccount, amount: Double, date: LocalDateTime, description: String) =
        saveTransferUseCase.execute(source, destination, amount, date, description)
    fun deleteTransaction(transaction: Transaction) = transactionRepository.delete(transaction)
    fun fetchTransactions(account: BankAccount? = null) = fetchTransactionsUseCase.execute(account)
    fun exportTransactionsToCsv(transactions: List<Transaction>, file: File) =
        exportTransactionsToCsvUseCase.execute(transactions, file)
}

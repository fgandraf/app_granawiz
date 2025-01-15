package service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.Transaction
import core.entity.account.BankAccount
import infra.dao.TransactionDao

class TransactionService {

    private val transactionDao : TransactionDao = TransactionDao()

    var transactions by mutableStateOf(emptyList<Transaction>())

    fun loadTransactions(account: BankAccount? = null) : List<Transaction> {
        return if (account == null) transactionDao.getAll()
        else transactionDao.getAllByAccount(account)
    }

    fun addTransaction(transaction: Transaction) { transactionDao.insert(transaction) }

    fun updateTransaction(transaction: Transaction) { transactionDao.update(transaction) }

    fun deleteTransaction(transaction: Transaction) { transactionDao.delete(transaction) }
}
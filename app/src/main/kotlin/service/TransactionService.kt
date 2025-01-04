package service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.Transaction
import core.entity.account.BankAccount
import infra.dao.TransactionDao

class TransactionService {

    private val dao : TransactionDao = TransactionDao()

    var transactions by mutableStateOf(emptyList<Transaction>())

    fun loadTransactions(account: BankAccount? = null) {
        transactions = if (account == null) dao.getAll()
        else dao.getAllByAccount(account)
    }

    fun addTransaction(transaction: Transaction) { dao.insert(transaction) }

    fun updateTransaction(transaction: Transaction) { dao.update(transaction) }

    fun deleteTransaction(transaction: Transaction) { dao.delete(transaction) }
}
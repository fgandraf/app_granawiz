package domain.transaction.usecases

import core.entity.Transaction
import core.entity.account.BankAccount
import infra.dao.TransactionDao

class FetchTransactionsUseCase(private val transactionDao: TransactionDao = TransactionDao()) {

    fun execute(account: BankAccount? = null): List<Transaction> {
        return if (account == null) transactionDao.getAll()
        else transactionDao.getAllByAccount(account)
    }

}
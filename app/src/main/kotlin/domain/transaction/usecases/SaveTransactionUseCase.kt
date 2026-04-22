package domain.transaction.usecases

import core.entity.Transaction
import infra.dao.TransactionDao

class SaveTransactionUseCase(private val transactionDao: TransactionDao = TransactionDao()) {

    fun execute(transaction: Transaction) {
        if (transaction.id == 0L) transactionDao.insert(transaction)
        else transactionDao.update(transaction)
    }

}

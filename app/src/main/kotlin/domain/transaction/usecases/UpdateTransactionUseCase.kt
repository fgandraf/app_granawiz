package domain.transaction.usecases

import core.entity.Transaction
import infra.dao.TransactionDao

class UpdateTransactionUseCase(private val transactionDao: TransactionDao = TransactionDao()) {

    fun execute(transaction: Transaction) {
        transactionDao.update(transaction)
    }

}
package domain.transaction.usecases

import core.entity.Transaction
import infra.dao.TransactionDao

class DeleteTransactionUseCase(private val transactionDao: TransactionDao = TransactionDao()) {

    fun execute(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

}
package application.transaction.usecases

import domain.entity.Transaction
import infrastructure.repository.TransactionRepository

class DeleteTransactionUseCase(private val transactionRepository: TransactionRepository = TransactionRepository()) {

    fun execute(transaction: Transaction) {
        transactionRepository.delete(transaction)
    }

}
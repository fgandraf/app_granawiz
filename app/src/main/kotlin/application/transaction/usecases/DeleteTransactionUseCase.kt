package application.transaction.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Transaction
import infrastructure.repository.TransactionRepository

class DeleteTransactionUseCase(private val transactionRepository: ITransactionRepository = TransactionRepository()) {

    fun execute(transaction: Transaction) {
        transactionRepository.delete(transaction)
    }

}
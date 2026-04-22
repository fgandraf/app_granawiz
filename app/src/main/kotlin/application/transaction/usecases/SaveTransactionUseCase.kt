package application.transaction.usecases

import domain.entity.Transaction
import infrastructure.repository.TransactionRepository

class SaveTransactionUseCase(private val transactionRepository: TransactionRepository = TransactionRepository()) {

    fun execute(transaction: Transaction) {
        if (transaction.id == 0L) transactionRepository.insert(transaction)
        else transactionRepository.update(transaction)
    }

}

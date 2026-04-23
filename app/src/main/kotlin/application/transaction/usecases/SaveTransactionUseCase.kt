package application.transaction.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Transaction
import infrastructure.repository.TransactionRepository

class SaveTransactionUseCase(private val transactionRepository: ITransactionRepository = TransactionRepository()) {

    fun execute(transaction: Transaction) {
        if (transaction.id == 0L) transactionRepository.insert(transaction)
        else transactionRepository.update(transaction)
    }

}

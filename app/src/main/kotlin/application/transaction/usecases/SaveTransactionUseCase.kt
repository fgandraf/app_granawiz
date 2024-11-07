package application.transaction.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Transaction
class SaveTransactionUseCase(private val transactionRepository: ITransactionRepository) {

    fun execute(transaction: Transaction) {
        if (transaction.id == 0L) transactionRepository.insert(transaction)
        else transactionRepository.update(transaction)
    }

}

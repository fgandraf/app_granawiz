package application.dashboard.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Transaction
import domain.enums.TransactionType
import java.time.LocalDateTime

class FetchTopTransactionsUseCase(private val transactionRepository: ITransactionRepository) {

    fun execute(
        from: LocalDateTime,
        to: LocalDateTime,
        type: TransactionType = TransactionType.EXPENSE,
        limit: Int = 3,
    ): List<Transaction> {
        return transactionRepository.getByDateRange(from, to, type)
            .sortedByDescending { kotlin.math.abs(it.balance) }
            .take(limit)
    }
}

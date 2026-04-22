package application.dashboard.usecases

import domain.entity.Transaction
import domain.enums.TransactionType
import infrastructure.repository.TransactionRepository
import java.time.LocalDateTime

class FetchTopTransactionsUseCase(private val transactionRepository: TransactionRepository = TransactionRepository()) {

    fun execute(
        from: LocalDateTime,
        to: LocalDateTime,
        type: TransactionType = TransactionType.EXPENSE,
        limit: Int = 5,
    ): List<Transaction> {
        return transactionRepository.getByDateRange(from, to, type)
            .sortedByDescending { kotlin.math.abs(it.balance) }
            .take(limit)
    }
}

package domain.dashboard.usecases

import core.entity.Transaction
import core.enums.TransactionType
import infra.dao.TransactionDao
import java.time.LocalDateTime

class FetchTopTransactionsUseCase(private val transactionDao: TransactionDao = TransactionDao()) {

    fun execute(
        from: LocalDateTime,
        to: LocalDateTime,
        type: TransactionType = TransactionType.EXPENSE,
        limit: Int = 5,
    ): List<Transaction> {
        return transactionDao.getByDateRange(from, to, type)
            .sortedByDescending { kotlin.math.abs(it.balance) }
            .take(limit)
    }
}

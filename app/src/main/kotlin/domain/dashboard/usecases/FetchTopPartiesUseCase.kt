package domain.dashboard.usecases

import core.enums.TransactionType
import core.structs.PartyVolume
import infra.dao.TransactionDao
import java.time.LocalDateTime

class FetchTopPartiesUseCase(private val transactionDao: TransactionDao = TransactionDao()) {

    fun execute(
        from: LocalDateTime,
        to: LocalDateTime,
        type: TransactionType = TransactionType.EXPENSE,
        limit: Int = 5,
    ): List<PartyVolume> {
        return transactionDao.getByDateRange(from, to, type)
            .groupBy { it.party }
            .map { (party, txs) ->
                PartyVolume(
                    party = party,
                    amount = txs.sumOf { kotlin.math.abs(it.balance) },
                    transactionCount = txs.size,
                )
            }
            .sortedByDescending { it.amount }
            .take(limit)
    }
}

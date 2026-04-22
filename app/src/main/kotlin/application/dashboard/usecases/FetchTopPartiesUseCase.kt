package application.dashboard.usecases

import domain.enums.TransactionType
import domain.structs.PartyVolume
import infrastructure.repository.TransactionRepository
import java.time.LocalDateTime

class FetchTopPartiesUseCase(private val transactionRepository: TransactionRepository = TransactionRepository()) {

    fun execute(
        from: LocalDateTime,
        to: LocalDateTime,
        type: TransactionType = TransactionType.EXPENSE,
        limit: Int = 5,
    ): List<PartyVolume> {
        return transactionRepository.getByDateRange(from, to, type)
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

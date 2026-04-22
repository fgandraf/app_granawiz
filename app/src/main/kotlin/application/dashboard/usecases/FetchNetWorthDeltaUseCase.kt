package application.dashboard.usecases

import domain.enums.TransactionType
import domain.structs.NetWorthSnapshot
import infrastructure.repository.GroupRepository
import infrastructure.repository.TransactionRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

class FetchNetWorthDeltaUseCase(
    private val groupRepository: GroupRepository = GroupRepository(),
    private val transactionRepository: TransactionRepository = TransactionRepository(),
) {

    fun execute(today: LocalDate = LocalDate.now()): NetWorthSnapshot {
        val total = groupRepository.getAll().sumOf { group -> group.accounts.sumOf { it.balance } }

        val monthStart = YearMonth.from(today).atDay(1).atStartOfDay()
        val monthEnd = today.atTime(LocalTime.MAX)
        val monthTxs = transactionRepository.getByDateRange(monthStart, monthEnd)
        val monthDelta = monthTxs.sumOf {
            when (it.type) {
                TransactionType.GAIN -> kotlin.math.abs(it.balance)
                TransactionType.EXPENSE -> -kotlin.math.abs(it.balance)
                TransactionType.NEUTRAL -> 0.0
            }
        }

        val startOfMonthTotal = total - monthDelta
        val deltaPercent = if (startOfMonthTotal != 0.0) monthDelta / startOfMonthTotal * 100.0 else 0.0

        return NetWorthSnapshot(
            total = total,
            deltaAmount = monthDelta,
            deltaPercent = deltaPercent,
        )
    }
}

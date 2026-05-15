package application.dashboard.usecases

import domain.contracts.ITransactionRepository
import domain.enums.TransactionType
import domain.structs.SpendingPace
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

class FetchSpendingPaceUseCase(private val transactionRepository: ITransactionRepository) {

    fun execute(
        today: LocalDate = LocalDate.now(),
        baselineMonths: Int = 3,
    ): SpendingPace? {
        val currentYm = YearMonth.from(today)
        val dayOfMonth = today.dayOfMonth

        val mtdStart = currentYm.atDay(1).atStartOfDay()
        val mtdEnd = today.atTime(LocalTime.MAX)
        val monthTotal = transactionRepository.getByDateRange(mtdStart, mtdEnd, TransactionType.EXPENSE)
            .sumOf { kotlin.math.abs(it.balance) }

        val baselineStart = currentYm.minusMonths(baselineMonths.toLong()).atDay(1).atStartOfDay()
        val baselineEnd = currentYm.minusMonths(1).atEndOfMonth().atTime(LocalTime.MAX)
        val baselineTxs = transactionRepository.getByDateRange(baselineStart, baselineEnd, TransactionType.EXPENSE)

        if (baselineTxs.isEmpty()) return null

        val totalsPerMonth = (1..baselineMonths).map { back ->
            val ym = currentYm.minusMonths(back.toLong())
            val cap = ym.atDay(dayOfMonth.coerceAtMost(ym.lengthOfMonth())).atTime(LocalTime.MAX)
            baselineTxs.filter { YearMonth.from(it.date) == ym && !it.date.isAfter(cap) }
                .sumOf { kotlin.math.abs(it.balance) }
        }

        val average = totalsPerMonth.average()
        val percent = if (average > 0.0) monthTotal / average * 100.0 else 0.0

        return SpendingPace(
            today = today,
            monthTotalSoFar = monthTotal,
            averageAtSameDay = average,
            percentOfAverage = percent,
        )
    }
}

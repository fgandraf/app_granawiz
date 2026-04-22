package application.dashboard.usecases

import domain.enums.TransactionType
import domain.structs.MonthlyFlow
import infrastructure.repository.TransactionRepository
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

class FetchMonthlyFlowUseCase(private val transactionRepository: TransactionRepository = TransactionRepository()) {

    enum class Granularity { MONTH, YEAR }

    fun execute(
        from: LocalDateTime,
        to: LocalDateTime,
        granularity: Granularity = Granularity.MONTH,
        maxBuckets: Int = 60,
    ): List<MonthlyFlow> = when (granularity) {
        Granularity.MONTH -> byMonth(from, to, maxBuckets)
        Granularity.YEAR -> byYear(from, to)
    }

    private fun byMonth(from: LocalDateTime, to: LocalDateTime, maxMonths: Int): List<MonthlyFlow> {
        val ptBr = Locale.of("pt", "BR")
        val endYm = YearMonth.from(to)
        val rangeStartYm = YearMonth.from(from)
        val totalMonths = (ChronoUnit.MONTHS.between(rangeStartYm, endYm) + 1).toInt().coerceAtLeast(1)
        val count = minOf(totalMonths, maxMonths)
        val startYm = endYm.minusMonths((count - 1).toLong())

        val queryFrom = startYm.atDay(1).atStartOfDay()
        val transactions = transactionRepository.getByDateRange(queryFrom, to)
            .filter { it.type == TransactionType.GAIN || it.type == TransactionType.EXPENSE }
        val grouped = transactions.groupBy { YearMonth.from(it.date) }

        return (0 until count).map { offset ->
            val ym = startYm.plusMonths(offset.toLong())
            val txs = grouped[ym].orEmpty()
            MonthlyFlow(
                label = ym.month.getDisplayName(TextStyle.SHORT, ptBr).replaceFirstChar { it.uppercase() },
                income = txs.filter { it.type == TransactionType.GAIN }.sumOf { kotlin.math.abs(it.balance) },
                expense = txs.filter { it.type == TransactionType.EXPENSE }.sumOf { kotlin.math.abs(it.balance) },
            )
        }
    }

    private fun byYear(from: LocalDateTime, to: LocalDateTime): List<MonthlyFlow> {
        val startYear = from.year
        val endYear = to.year
        val transactions = transactionRepository.getByDateRange(from, to)
            .filter { it.type == TransactionType.GAIN || it.type == TransactionType.EXPENSE }
        val grouped = transactions.groupBy { it.date.year }

        return (startYear..endYear).map { year ->
            val txs = grouped[year].orEmpty()
            MonthlyFlow(
                label = year.toString(),
                income = txs.filter { it.type == TransactionType.GAIN }.sumOf { kotlin.math.abs(it.balance) },
                expense = txs.filter { it.type == TransactionType.EXPENSE }.sumOf { kotlin.math.abs(it.balance) },
            )
        }
    }
}

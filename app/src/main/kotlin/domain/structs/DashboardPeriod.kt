package domain.structs

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

sealed class DashboardPeriod(val label: String) {

    abstract fun range(today: LocalDate = LocalDate.now()): Pair<LocalDateTime, LocalDateTime>

    data object ThisMonth : DashboardPeriod("Este mês") {
        override fun range(today: LocalDate): Pair<LocalDateTime, LocalDateTime> {
            val ym = YearMonth.from(today)
            return ym.atDay(1).atStartOfDay() to ym.atEndOfMonth().atTime(LocalTime.MAX)
        }
    }

    data object LastMonth : DashboardPeriod("Mês passado") {
        override fun range(today: LocalDate): Pair<LocalDateTime, LocalDateTime> {
            val ym = YearMonth.from(today).minusMonths(1)
            return ym.atDay(1).atStartOfDay() to ym.atEndOfMonth().atTime(LocalTime.MAX)
        }
    }

    data object Last3Months : DashboardPeriod("Últimos 3 meses") {
        override fun range(today: LocalDate): Pair<LocalDateTime, LocalDateTime> {
            val start = YearMonth.from(today).minusMonths(2).atDay(1).atStartOfDay()
            val end = YearMonth.from(today).atEndOfMonth().atTime(LocalTime.MAX)
            return start to end
        }
    }

    data object ThisYear : DashboardPeriod("Este ano") {
        override fun range(today: LocalDate): Pair<LocalDateTime, LocalDateTime> {
            val start = LocalDate.of(today.year, 1, 1).atStartOfDay()
            val end = LocalDate.of(today.year, 12, 31).atTime(LocalTime.MAX)
            return start to end
        }
    }

    data object Last3Years : DashboardPeriod("Últimos 3 anos") {
        override fun range(today: LocalDate): Pair<LocalDateTime, LocalDateTime> {
            val endYm = YearMonth.from(today)
            val startYm = endYm.minusMonths(35)
            return startYm.atDay(1).atStartOfDay() to endYm.atEndOfMonth().atTime(LocalTime.MAX)
        }
    }

    companion object {
        val all: List<DashboardPeriod> by lazy { listOf(ThisMonth, LastMonth, Last3Months, ThisYear, Last3Years) }
    }
}

package application.schedule.usecases

import domain.entity.Schedule
import domain.entity.Transaction
import domain.enums.ScheduleFrequency
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

data class ScheduleOccurrence(
    val schedule: Schedule,
    val dueDate: LocalDateTime,
    val index: Int,
)

class GenerateOccurrencesUseCase {

    fun execute(
        schedule: Schedule,
        windowStart: LocalDateTime,
        windowEnd: LocalDateTime,
        paidTransactions: List<Transaction> = emptyList(),
    ): List<ScheduleOccurrence> {
        val paidKeys = paidTransactions
            .filter { it.scheduleId == schedule.id && it.originalDueDate != null }
            .map { it.originalDueDate!! }
            .toSet()

        val hardEnd = schedule.endDate ?: windowEnd
        val limit = if (hardEnd.isBefore(windowEnd)) hardEnd else windowEnd
        val maxInstallments = schedule.installments

        val occurrences = mutableListOf<ScheduleOccurrence>()
        var index = 0
        var current = schedule.startDate

        while (!current.isAfter(limit)) {
            if (maxInstallments != null && index >= maxInstallments) break

            if (!current.isBefore(windowStart) && current !in paidKeys) {
                occurrences.add(ScheduleOccurrence(schedule, current, index))
            }

            index++
            current = nextDate(schedule, index)
        }

        return occurrences
    }

    private fun nextDate(schedule: Schedule, index: Int): LocalDateTime {
        val base = schedule.startDate
        val step = schedule.interval.coerceAtLeast(1)
        return when (schedule.frequency) {
            ScheduleFrequency.ONCE -> LocalDateTime.MAX
            ScheduleFrequency.DAILY -> base.plusDays((step * index).toLong())
            ScheduleFrequency.WEEKLY -> base.plusWeeks((step * index).toLong())
            ScheduleFrequency.MONTHLY -> monthlyDate(base, schedule.dayOfMonth, step, index)
            ScheduleFrequency.YEARLY -> base.plusYears((step * index).toLong())
        }
    }

    private fun monthlyDate(base: LocalDateTime, dayOfMonth: Int?, step: Int, index: Int): LocalDateTime {
        val target = base.plusMonths((step * index).toLong())
        val desiredDay = dayOfMonth ?: base.dayOfMonth
        val ym = YearMonth.of(target.year, target.month)
        val safeDay = desiredDay.coerceAtMost(ym.lengthOfMonth())
        return LocalDateTime.of(
            LocalDate.of(target.year, target.month, safeDay),
            base.toLocalTime()
        )
    }

}

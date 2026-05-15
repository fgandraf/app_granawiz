package application.schedule.usecases

import domain.contracts.IScheduleRepository
import domain.entity.Schedule
import domain.enums.ScheduleFrequency
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class DeleteScheduleUseCase(private val scheduleRepository: IScheduleRepository) {

    fun execute(schedule: Schedule) {
        scheduleRepository.delete(schedule)
    }

    fun deleteThisOccurrence(occurrence: ScheduleOccurrence) {
        val schedule = occurrence.schedule
        val index = occurrence.index

        if (schedule.frequency == ScheduleFrequency.ONCE) {
            scheduleRepository.delete(schedule)
            return
        }

        val installments = schedule.installments
        val isLastByInstallments = installments != null && index >= installments - 1
        val nextDate = if (isLastByInstallments) LocalDateTime.MAX else nextOccurrenceDate(schedule, index + 1)

        if (index == 0) {
            if (nextDate == LocalDateTime.MAX) {
                scheduleRepository.delete(schedule)
            } else {
                scheduleRepository.update(schedule.copy(
                    startDate = nextDate,
                    installments = schedule.installments?.let { it - 1 },
                ))
            }
        } else {
            scheduleRepository.update(schedule.copy(endDate = occurrence.dueDate.minusSeconds(1)))
            if (nextDate != LocalDateTime.MAX) {
                val adjustedInstallments = schedule.installments?.let { it - (index + 1) }
                if (adjustedInstallments == null || adjustedInstallments > 0) {
                    scheduleRepository.insert(schedule.copy(
                        id = 0,
                        startDate = nextDate,
                        endDate = schedule.endDate,
                        installments = adjustedInstallments,
                    ))
                }
            }
        }
    }

    fun deleteThisAndFuture(occurrence: ScheduleOccurrence) {
        val schedule = occurrence.schedule
        if (occurrence.index == 0) {
            scheduleRepository.delete(schedule)
        } else {
            scheduleRepository.update(schedule.copy(endDate = occurrence.dueDate.minusSeconds(1)))
        }
    }

    private fun nextOccurrenceDate(schedule: Schedule, index: Int): LocalDateTime {
        val base = schedule.startDate
        val step = schedule.interval.coerceAtLeast(1)
        return when (schedule.frequency) {
            ScheduleFrequency.ONCE -> LocalDateTime.MAX
            ScheduleFrequency.DAILY -> base.plusDays((step * index).toLong())
            ScheduleFrequency.WEEKLY -> base.plusWeeks((step * index).toLong())
            ScheduleFrequency.MONTHLY -> {
                val target = base.plusMonths((step * index).toLong())
                val desiredDay = schedule.dayOfMonth ?: base.dayOfMonth
                val ym = YearMonth.of(target.year, target.month)
                val safeDay = desiredDay.coerceAtMost(ym.lengthOfMonth())
                LocalDateTime.of(LocalDate.of(target.year, target.month, safeDay), base.toLocalTime())
            }
            ScheduleFrequency.YEARLY -> base.plusYears((step * index).toLong())
        }
    }
}

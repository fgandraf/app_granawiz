package domain.schedule.usecases

import core.entity.Schedule
import core.enums.ScheduleFrequency
import infra.dao.ScheduleDao
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class DeleteScheduleUseCase(private val scheduleDao: ScheduleDao = ScheduleDao()) {

    fun execute(schedule: Schedule) {
        scheduleDao.delete(schedule)
    }

    fun deleteThisOccurrence(occurrence: ScheduleOccurrence) {
        val schedule = occurrence.schedule
        val index = occurrence.index

        if (schedule.frequency == ScheduleFrequency.ONCE) {
            scheduleDao.delete(schedule)
            return
        }

        val installments = schedule.installments
        val isLastByInstallments = installments != null && index >= installments - 1
        val nextDate = if (isLastByInstallments) LocalDateTime.MAX else nextOccurrenceDate(schedule, index + 1)

        if (index == 0) {
            if (nextDate == LocalDateTime.MAX) {
                scheduleDao.delete(schedule)
            } else {
                scheduleDao.update(schedule.copy(
                    startDate = nextDate,
                    installments = schedule.installments?.let { it - 1 },
                ))
            }
        } else {
            scheduleDao.update(schedule.copy(endDate = occurrence.dueDate.minusSeconds(1)))
            if (nextDate != LocalDateTime.MAX) {
                val adjustedInstallments = schedule.installments?.let { it - (index + 1) }
                if (adjustedInstallments == null || adjustedInstallments > 0) {
                    scheduleDao.insert(schedule.copy(
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
            scheduleDao.delete(schedule)
        } else {
            scheduleDao.update(schedule.copy(endDate = occurrence.dueDate.minusSeconds(1)))
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

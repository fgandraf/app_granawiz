package utils

import java.time.LocalDateTime
import java.time.YearMonth

fun computeBillingYearMonth(
    date: LocalDateTime,
    originalDueDate: LocalDateTime?,
    scheduleId: Long?,
    closingDay: Int,
): YearMonth {
    if (scheduleId == null && originalDueDate != null)
        return YearMonth.from(originalDueDate)
    val effectiveDate = originalDueDate ?: date
    val txYearMonth = YearMonth.from(effectiveDate)
    return if (effectiveDate.dayOfMonth < closingDay)
        txYearMonth.plusMonths(1) else txYearMonth.plusMonths(2)
}

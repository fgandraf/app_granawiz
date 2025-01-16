package utils

import java.time.LocalDate
import java.time.YearMonth


fun generateWeeks(currentMonth: YearMonth): List<List<LocalDate?>> {

    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7 // Ajuste para começar no domingo
    return mutableListOf<List<LocalDate?>>().apply {
        var week = mutableListOf<LocalDate?>()
        for (i in 0 until firstDayOfWeek) {
            week.add(null)
        }
        for (day in 1..daysInMonth) {
            week.add(currentMonth.atDay(day))
            if (week.size == 7) {
                add(week)
                week = mutableListOf()
            }
        }
        if (week.isNotEmpty()) {
            while (week.size < 7) {
                week.add(null)
            }
            add(week)
        }
    }
}
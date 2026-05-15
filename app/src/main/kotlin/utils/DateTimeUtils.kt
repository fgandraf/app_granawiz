package utils

import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale
import viewModel.UserPreferences

fun generateWeeks(
    currentMonth: YearMonth,
    locale: Locale = Locale.forLanguageTag(UserPreferences.language)
): List<List<LocalDate?>> {

    val daysInMonth = currentMonth.lengthOfMonth()
    val localeFirstDay = WeekFields.of(locale).firstDayOfWeek
    val firstDayOfWeek = (currentMonth.atDay(1).dayOfWeek.value - localeFirstDay.value + 7) % 7
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
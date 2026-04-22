package domain.dashboard.usecases

import domain.schedule.usecases.FetchSchedulesUseCase
import domain.schedule.usecases.GenerateOccurrencesUseCase
import domain.schedule.usecases.ScheduleOccurrence
import infra.dao.TransactionDao
import java.time.LocalDate

class FetchUpcomingOccurrencesUseCase(
    private val fetchSchedulesUseCase: FetchSchedulesUseCase = FetchSchedulesUseCase(),
    private val generateOccurrencesUseCase: GenerateOccurrencesUseCase = GenerateOccurrencesUseCase(),
    private val transactionDao: TransactionDao = TransactionDao(),
) {

    fun execute(today: LocalDate = LocalDate.now(), limit: Int = 3): List<ScheduleOccurrence> {
        val windowStart = today.atStartOfDay()
        val windowEnd = today.plusMonths(3).atStartOfDay()
        val paidTxs = transactionDao.getAll()

        return fetchSchedulesUseCase.execute()
            .flatMap { generateOccurrencesUseCase.execute(it, windowStart, windowEnd, paidTxs) }
            .sortedBy { it.dueDate }
            .take(limit)
    }
}

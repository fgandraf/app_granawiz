package application.dashboard.usecases

import application.schedule.usecases.FetchSchedulesUseCase
import application.schedule.usecases.GenerateOccurrencesUseCase
import application.schedule.usecases.ScheduleOccurrence
import domain.contracts.ITransactionRepository
import infrastructure.repository.TransactionRepository
import java.time.LocalDate

class FetchUpcomingOccurrencesUseCase(
    private val fetchSchedulesUseCase: FetchSchedulesUseCase = FetchSchedulesUseCase(),
    private val generateOccurrencesUseCase: GenerateOccurrencesUseCase = GenerateOccurrencesUseCase(),
    private val transactionRepository: ITransactionRepository = TransactionRepository(),
) {

    fun execute(today: LocalDate = LocalDate.now(), limit: Int = 5): List<ScheduleOccurrence> {
        val windowStart = today.atStartOfDay()
        val windowEnd = today.plusMonths(3).atStartOfDay()
        val paidTxs = transactionRepository.getAll()

        return fetchSchedulesUseCase.execute()
            .flatMap { generateOccurrencesUseCase.execute(it, windowStart, windowEnd, paidTxs) }
            .sortedBy { it.dueDate }
            .take(limit)
    }
}

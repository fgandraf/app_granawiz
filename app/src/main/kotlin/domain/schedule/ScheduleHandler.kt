package domain.schedule

import core.entity.Schedule
import core.entity.Transaction
import core.entity.account.BankAccount
import domain.schedule.usecases.DeleteScheduleUseCase
import domain.schedule.usecases.FetchSchedulesUseCase
import domain.schedule.usecases.GenerateOccurrencesUseCase
import domain.schedule.usecases.MarkAsPaidUseCase
import domain.schedule.usecases.SaveScheduleUseCase
import domain.schedule.usecases.ScheduleOccurrence
import viewModel.TransactionFormViewModel
import java.time.LocalDateTime

class ScheduleHandler {

    private val saveScheduleUseCase = SaveScheduleUseCase()
    private val deleteScheduleUseCase = DeleteScheduleUseCase()
    private val fetchSchedulesUseCase = FetchSchedulesUseCase()
    private val generateOccurrencesUseCase = GenerateOccurrencesUseCase()
    private val markAsPaidUseCase = MarkAsPaidUseCase()

    fun saveSchedule(viewModel: TransactionFormViewModel) = saveScheduleUseCase.execute(viewModel)
    fun deleteSchedule(schedule: Schedule) = deleteScheduleUseCase.execute(schedule)
    fun fetchSchedules(account: BankAccount? = null) = fetchSchedulesUseCase.execute(account)
    fun markAsPaid(schedule: Schedule, dueDate: LocalDateTime) = markAsPaidUseCase.execute(schedule, dueDate)

    fun generateOccurrences(
        schedule: Schedule,
        windowStart: LocalDateTime,
        windowEnd: LocalDateTime,
        paidTransactions: List<Transaction>,
    ): List<ScheduleOccurrence> = generateOccurrencesUseCase.execute(schedule, windowStart, windowEnd, paidTransactions)

}

package application.schedule

import domain.entity.Schedule
import domain.entity.Transaction
import domain.entity.account.BankAccount
import application.schedule.usecases.DeleteScheduleUseCase
import application.schedule.usecases.FetchSchedulesUseCase
import application.schedule.usecases.GenerateOccurrencesUseCase
import application.schedule.usecases.MarkAsPaidUseCase
import application.schedule.usecases.SaveScheduleUseCase
import application.schedule.usecases.ScheduleOccurrence
import viewModel.TransactionFormViewModel
import java.time.LocalDateTime

class ScheduleHandler {

    private val saveScheduleUseCase = SaveScheduleUseCase()
    private val deleteScheduleUseCase = DeleteScheduleUseCase()
    private val fetchSchedulesUseCase = FetchSchedulesUseCase()
    private val generateOccurrencesUseCase = GenerateOccurrencesUseCase()
    private val markAsPaidUseCase = MarkAsPaidUseCase()

    fun saveSchedule(viewModel: TransactionFormViewModel) = saveScheduleUseCase.execute(viewModel)
    fun deleteThisOccurrence(occurrence: ScheduleOccurrence) = deleteScheduleUseCase.deleteThisOccurrence(occurrence)
    fun deleteThisAndFuture(occurrence: ScheduleOccurrence) = deleteScheduleUseCase.deleteThisAndFuture(occurrence)
    fun fetchSchedules(account: BankAccount? = null) = fetchSchedulesUseCase.execute(account)
    fun markAsPaid(schedule: Schedule, dueDate: LocalDateTime) = markAsPaidUseCase.execute(schedule, dueDate)

    fun generateOccurrences(
        schedule: Schedule,
        windowStart: LocalDateTime,
        windowEnd: LocalDateTime,
        paidTransactions: List<Transaction>,
    ): List<ScheduleOccurrence> = generateOccurrencesUseCase.execute(schedule, windowStart, windowEnd, paidTransactions)

}

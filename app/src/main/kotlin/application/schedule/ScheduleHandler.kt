package application.schedule

import domain.contracts.IScheduleRepository
import domain.entity.Schedule
import domain.entity.Transaction
import domain.entity.account.BankAccount
import application.schedule.usecases.DeleteScheduleUseCase
import application.schedule.usecases.ExportSchedulesToExcelUseCase
import application.schedule.usecases.GenerateOccurrencesUseCase
import application.schedule.usecases.MarkAsPaidUseCase
import application.schedule.usecases.SaveScheduleUseCase
import application.schedule.usecases.ScheduleOccurrence
import infrastructure.repository.ScheduleRepository
import java.io.File
import java.time.LocalDateTime

class ScheduleHandler {

    private val scheduleRepository: IScheduleRepository = ScheduleRepository()
    private val saveScheduleUseCase = SaveScheduleUseCase()
    private val deleteScheduleUseCase = DeleteScheduleUseCase()
    private val generateOccurrencesUseCase = GenerateOccurrencesUseCase()
    private val markAsPaidUseCase = MarkAsPaidUseCase()
    private val exportSchedulesToExcelUseCase = ExportSchedulesToExcelUseCase()

    fun saveSchedule(schedule: Schedule) = saveScheduleUseCase.execute(schedule)
    fun deleteThisOccurrence(occurrence: ScheduleOccurrence) = deleteScheduleUseCase.deleteThisOccurrence(occurrence)
    fun deleteThisAndFuture(occurrence: ScheduleOccurrence) = deleteScheduleUseCase.deleteThisAndFuture(occurrence)
    fun fetchSchedules(account: BankAccount? = null): List<Schedule> =
        if (account == null) scheduleRepository.getAll() else scheduleRepository.getAllByAccount(account)
    fun markAsPaid(schedule: Schedule, dueDate: LocalDateTime, occurrenceIndex: Int? = null) =
        markAsPaidUseCase.execute(schedule, dueDate, occurrenceIndex)
    fun exportSchedulesToExcel(occurrences: List<ScheduleOccurrence>, file: File) =
        exportSchedulesToExcelUseCase.execute(occurrences, file)
    fun generateOccurrences(
        schedule: Schedule,
        windowStart: LocalDateTime,
        windowEnd: LocalDateTime,
        paidTransactions: List<Transaction>,
    ): List<ScheduleOccurrence> = generateOccurrencesUseCase.execute(schedule, windowStart, windowEnd, paidTransactions)
}

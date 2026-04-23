package application.schedule.usecases

import domain.contracts.IScheduleRepository
import domain.contracts.ITransactionRepository
import domain.entity.Schedule
import domain.entity.Transaction
import domain.enums.ScheduleFrequency
import infrastructure.repository.ScheduleRepository
import infrastructure.repository.TransactionRepository
import java.time.LocalDate

class SaveScheduleUseCase(
    private val scheduleRepository: IScheduleRepository = ScheduleRepository(),
    private val transactionRepository: ITransactionRepository = TransactionRepository(),
    private val markAsPaidUseCase: MarkAsPaidUseCase = MarkAsPaidUseCase(),
) {

    fun execute(schedule: Schedule) {
        if (schedule.id == 0L) {
            val isPastOrToday = !schedule.startDate.toLocalDate().isAfter(LocalDate.now())

            if (isPastOrToday && schedule.frequency == ScheduleFrequency.ONCE) {
                transactionRepository.insert(
                    Transaction(
                        id = 0,
                        party = schedule.party,
                        account = schedule.account,
                        category = schedule.category,
                        subcategory = schedule.subcategory,
                        tags = schedule.tags,
                        date = schedule.startDate,
                        description = schedule.description,
                        balance = schedule.balance,
                        type = schedule.type,
                    )
                )
            } else {
                val saved = scheduleRepository.insert(schedule)
                if (isPastOrToday) {
                    markAsPaidUseCase.execute(saved, saved.startDate)
                }
            }
        } else {
            scheduleRepository.update(schedule)
        }
    }

}

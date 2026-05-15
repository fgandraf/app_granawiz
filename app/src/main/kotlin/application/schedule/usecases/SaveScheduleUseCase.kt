package application.schedule.usecases

import domain.contracts.IScheduleRepository
import domain.contracts.ITransactionRepository
import domain.entity.Schedule
import domain.entity.Transaction
import domain.enums.ScheduleFrequency
import infrastructure.config.transactional
import infrastructure.repository.ScheduleRepository
import infrastructure.repository.TransactionRepository
import java.time.LocalDate

class SaveScheduleUseCase(
    private val scheduleRepository: IScheduleRepository = ScheduleRepository(),
    private val transactionRepository: ITransactionRepository = TransactionRepository(),
) {
    fun execute(schedule: Schedule) {
        if (schedule.id == 0L) {
            val isPastOrToday = !schedule.startDate.toLocalDate().isAfter(LocalDate.now())

            if (isPastOrToday && schedule.frequency == ScheduleFrequency.ONCE) {
                transactionRepository.insert(Transaction(
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
                    installment = "1/1",
                ))
            } else {
                transactional { session ->
                    val saved = session.merge(schedule)
                    if (isPastOrToday) {
                        session.persist(Transaction(
                            id = 0,
                            party = saved.party,
                            account = saved.account,
                            category = saved.category,
                            subcategory = saved.subcategory,
                            tags = saved.tags,
                            date = saved.startDate,
                            description = saved.description,
                            balance = saved.balance,
                            type = saved.type,
                            scheduleId = saved.id,
                            originalDueDate = saved.startDate,
                            installment = "1/${saved.installments ?: 1}",
                        ))
                    }
                }
            }
        } else {
            scheduleRepository.update(schedule)
        }
    }
}

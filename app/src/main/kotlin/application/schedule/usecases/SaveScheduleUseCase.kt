package application.schedule.usecases

import domain.contracts.IScheduleRepository
import domain.contracts.ITransactionRepository
import domain.entity.Schedule
import domain.entity.Transaction
import domain.entity.account.CreditCardAccount
import domain.enums.ScheduleFrequency
import infrastructure.config.transactional
import utils.computeBillingYearMonth
import java.time.LocalDate

class SaveScheduleUseCase(
    private val scheduleRepository: IScheduleRepository,
    private val transactionRepository: ITransactionRepository,
) {
    fun execute(schedule: Schedule) {
        if (schedule.id == 0L) {
            val isPastOrToday = !schedule.startDate.toLocalDate().isAfter(LocalDate.now())

            if (isPastOrToday && schedule.frequency == ScheduleFrequency.ONCE) {
                val creditCard = schedule.account as? CreditCardAccount
                val billingYearMonth = creditCard?.let {
                    computeBillingYearMonth(schedule.startDate, null, null, it.closingDay).toString()
                }
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
                    billingYearMonth = billingYearMonth,
                ))
            } else {
                transactional { session ->
                    val saved = session.merge(schedule)
                    if (isPastOrToday) {
                        val creditCard = saved.account as? CreditCardAccount
                        val billingYearMonth = creditCard?.let {
                            computeBillingYearMonth(saved.startDate, saved.startDate, saved.id, it.closingDay).toString()
                        }
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
                            billingYearMonth = billingYearMonth,
                        ))
                    }
                }
            }
        } else {
            scheduleRepository.update(schedule)
        }
    }
}

package domain.schedule.usecases

import core.entity.Schedule
import core.entity.Transaction
import core.enums.ScheduleFrequency
import infra.dao.ScheduleDao
import infra.dao.TransactionDao
import viewModel.TransactionFormViewModel
import java.time.LocalDate

class SaveScheduleUseCase(
    private val scheduleDao: ScheduleDao = ScheduleDao(),
    private val transactionDao: TransactionDao = TransactionDao(),
    private val markAsPaidUseCase: MarkAsPaidUseCase = MarkAsPaidUseCase(),
) {

    fun execute(viewModel: TransactionFormViewModel) {
        val schedule = Schedule(
            id = viewModel.id,
            party = viewModel.party.value!!,
            account = viewModel.account,
            category = viewModel.category.value!!,
            subcategory = viewModel.subCategory,
            tags = viewModel.tags.value,
            startDate = viewModel.startDate,
            description = viewModel.description,
            balance = viewModel.balance,
            type = viewModel.type,
            frequency = viewModel.frequency,
            interval = viewModel.interval,
            dayOfMonth = viewModel.dayOfMonth,
            endDate = viewModel.endDate,
            installments = viewModel.installments,
        )

        if (schedule.id == 0L) {
            val isPastOrToday = !schedule.startDate.toLocalDate().isAfter(LocalDate.now())

            if (isPastOrToday && schedule.frequency == ScheduleFrequency.ONCE) {
                // One-time entry on today or in the past: create a standalone Transaction, skip Schedule
                transactionDao.insert(
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
                val saved = scheduleDao.insert(schedule)
                if (isPastOrToday) {
                    markAsPaidUseCase.execute(saved, saved.startDate)
                }
            }
        } else {
            scheduleDao.update(schedule)
        }
    }

}

package application.schedule.usecases

import domain.contracts.IScheduleRepository
import domain.contracts.ITransactionRepository
import domain.entity.Schedule
import domain.entity.Transaction
import domain.enums.ScheduleFrequency
import infrastructure.repository.ScheduleRepository
import infrastructure.repository.TransactionRepository
import viewModel.TransactionFormViewModel
import java.time.LocalDate

class SaveScheduleUseCase(
    private val scheduleRepository: IScheduleRepository = ScheduleRepository(),
    private val transactionRepository: ITransactionRepository = TransactionRepository(),
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

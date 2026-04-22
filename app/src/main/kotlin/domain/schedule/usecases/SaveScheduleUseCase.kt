package domain.schedule.usecases

import core.entity.Schedule
import infra.dao.ScheduleDao
import viewModel.ScheduleFormViewModel
import java.time.LocalDate

class SaveScheduleUseCase(
    private val scheduleDao: ScheduleDao = ScheduleDao(),
    private val markAsPaidUseCase: MarkAsPaidUseCase = MarkAsPaidUseCase(),
) {

    fun execute(viewModel: ScheduleFormViewModel) {
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
            val saved = scheduleDao.insert(schedule)
            if (!saved.startDate.toLocalDate().isAfter(LocalDate.now())) {
                markAsPaidUseCase.execute(saved, saved.startDate)
            }
        } else {
            scheduleDao.update(schedule)
        }
    }

}

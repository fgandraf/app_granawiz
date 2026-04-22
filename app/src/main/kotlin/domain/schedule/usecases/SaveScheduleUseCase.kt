package domain.schedule.usecases

import core.entity.Schedule
import infra.dao.ScheduleDao
import viewModel.ScheduleFormViewModel

class SaveScheduleUseCase(private val scheduleDao: ScheduleDao = ScheduleDao()) {

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

        if (schedule.id == 0L) scheduleDao.insert(schedule)
        else scheduleDao.update(schedule)
    }

}

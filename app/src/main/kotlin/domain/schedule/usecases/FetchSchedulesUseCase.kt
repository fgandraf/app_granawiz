package domain.schedule.usecases

import core.entity.Schedule
import core.entity.account.BankAccount
import infra.dao.ScheduleDao

class FetchSchedulesUseCase(private val scheduleDao: ScheduleDao = ScheduleDao()) {

    fun execute(account: BankAccount? = null): List<Schedule> {
        return if (account == null) scheduleDao.getAll()
        else scheduleDao.getAllByAccount(account)
    }

}

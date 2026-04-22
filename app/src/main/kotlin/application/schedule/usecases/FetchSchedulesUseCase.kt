package application.schedule.usecases

import domain.entity.Schedule
import domain.entity.account.BankAccount
import infrastructure.repository.ScheduleRepository

class FetchSchedulesUseCase(private val scheduleRepository: ScheduleRepository = ScheduleRepository()) {

    fun execute(account: BankAccount? = null): List<Schedule> {
        return if (account == null) scheduleRepository.getAll()
        else scheduleRepository.getAllByAccount(account)
    }

}

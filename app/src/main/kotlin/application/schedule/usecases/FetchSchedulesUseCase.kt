package application.schedule.usecases

import domain.contracts.IScheduleRepository
import domain.entity.Schedule
import domain.entity.account.BankAccount
class FetchSchedulesUseCase(private val scheduleRepository: IScheduleRepository) {

    fun execute(account: BankAccount? = null): List<Schedule> {
        return if (account == null) scheduleRepository.getAll()
        else scheduleRepository.getAllByAccount(account)
    }

}

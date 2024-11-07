package domain.contracts

import domain.entity.Schedule
import domain.entity.account.BankAccount

interface IScheduleRepository {

    fun getAll(): List<Schedule>

    fun getAllByAccount(account: BankAccount): List<Schedule>

    fun insert(schedule: Schedule): Schedule

    fun update(schedule: Schedule)

    fun delete(schedule: Schedule)

}

package core.contracts

import core.entity.Schedule
import core.entity.account.BankAccount

interface IScheduleDao {

    fun getAll(): List<Schedule>

    fun getAllByAccount(account: BankAccount): List<Schedule>

}

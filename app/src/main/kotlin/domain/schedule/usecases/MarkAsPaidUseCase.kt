package domain.schedule.usecases

import core.entity.Schedule
import core.entity.Transaction
import infra.dao.TransactionDao
import java.time.LocalDateTime

class MarkAsPaidUseCase(private val transactionDao: TransactionDao = TransactionDao()) {

    fun execute(schedule: Schedule, dueDate: LocalDateTime) {
        val transaction = Transaction(
            id = 0,
            party = schedule.party,
            account = schedule.account,
            category = schedule.category,
            subcategory = schedule.subcategory,
            tags = schedule.tags,
            date = dueDate,
            description = schedule.description,
            balance = schedule.balance,
            type = schedule.type,
            scheduleId = schedule.id,
            originalDueDate = dueDate,
        )
        transactionDao.insert(transaction)
    }

}

package application.schedule.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Schedule
import domain.entity.Transaction
import infrastructure.repository.TransactionRepository
import java.time.LocalDateTime

class MarkAsPaidUseCase(private val transactionRepository: ITransactionRepository = TransactionRepository()) {

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
        transactionRepository.insert(transaction)
    }

}

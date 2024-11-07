package application.schedule.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Schedule
import domain.entity.Transaction
import java.time.LocalDateTime

class MarkAsPaidUseCase(private val transactionRepository: ITransactionRepository) {

    fun execute(schedule: Schedule, dueDate: LocalDateTime, occurrenceIndex: Int? = null) {
        val totalInstallments = schedule.installments ?: 1
        val installment = if (occurrenceIndex != null) {
            "${occurrenceIndex + 1}/$totalInstallments"
        } else {
            val existingTransactions = transactionRepository.findByScheduleId(schedule.id)
            val currentInstallment = existingTransactions.size + 1
            "$currentInstallment/$totalInstallments"
        }

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
            installment = installment,
        )
        transactionRepository.insert(transaction)
    }

}

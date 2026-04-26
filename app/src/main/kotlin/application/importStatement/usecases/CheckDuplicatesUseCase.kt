package application.importStatement.usecases

import domain.contracts.ITransactionRepository
import domain.entity.account.BankAccount
import domain.structs.ParsedEntry
import infrastructure.repository.TransactionRepository

class CheckDuplicatesUseCase(
    private val transactionRepo: ITransactionRepository = TransactionRepository(),
) {
    fun execute(entries: List<ParsedEntry>, account: BankAccount): List<ParsedEntry> {
        val existing = transactionRepo.getAllByAccount(account)
        val existingKeys = existing.map { it.date.toLocalDate() to it.balance }.toSet()
        return entries.map { entry ->
            entry.copy(isPossibleDuplicate = (entry.date.toLocalDate() to entry.balance) in existingKeys)
        }
    }
}

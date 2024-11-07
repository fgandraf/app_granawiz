package application.importStatement.usecases

import domain.contracts.ITransactionRepository
import domain.entity.account.BankAccount
import domain.structs.ParsedEntry
class CheckDuplicatesUseCase(
    private val transactionRepo: ITransactionRepository,
) {
    fun execute(entries: List<ParsedEntry>, account: BankAccount): List<ParsedEntry> {
        val existing = transactionRepo.getAllByAccount(account)
        val existingKeys = existing.map { it.date.toLocalDate() to it.balance }.toSet()
        return entries.map { entry ->
            entry.copy(isPossibleDuplicate = (entry.date.toLocalDate() to entry.balance) in existingKeys)
        }
    }
}

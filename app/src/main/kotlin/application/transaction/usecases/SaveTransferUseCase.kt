package application.transaction.usecases

import domain.contracts.IAccountRepository
import domain.contracts.ICategoryRepository
import domain.contracts.IPartyRepository
import domain.contracts.ITransactionRepository
import domain.entity.Party
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.CategoryType
import domain.enums.PartyType
import domain.enums.TransactionType
import java.time.LocalDateTime
import kotlin.math.abs

class SaveTransferUseCase(
    private val categoryRepository: ICategoryRepository,
    private val partyRepository: IPartyRepository,
    private val transactionRepository: ITransactionRepository,
    private val accountRepository: IAccountRepository,
) {

    fun execute(source: BankAccount, destination: BankAccount, amount: Double, date: LocalDateTime, description: String) {
        val expenseCategory = categoryRepository.findByNameAndType("Transferência", CategoryType.EXPENSE)
            ?: error("Categoria 'Transferência' (EXPENSE) não encontrada")
        val incomeCategory = categoryRepository.findByNameAndType("Transferência", CategoryType.INCOME)
            ?: error("Categoria 'Transferência' (INCOME) não encontrada")

        val receiver = partyRepository.getPartyByNameAndType(destination.name, PartyType.RECEIVER)
            ?: Party(name = destination.name, type = PartyType.RECEIVER).also { partyRepository.insert(it) }
        val payer = partyRepository.getPartyByNameAndType(source.name, PartyType.PAYER)
            ?: Party(name = source.name, type = PartyType.PAYER).also { partyRepository.insert(it) }

        transactionRepository.insert(Transaction(
            party = receiver,
            account = source,
            category = expenseCategory,
            subcategory = null,
            date = date,
            description = description,
            balance = -abs(amount),
            type = TransactionType.EXPENSE,
            isTransfer = true,
        ))

        transactionRepository.insert(Transaction(
            party = payer,
            account = destination,
            category = incomeCategory,
            subcategory = null,
            date = date,
            description = description,
            balance = abs(amount),
            type = TransactionType.GAIN,
            isTransfer = true,
        ))

        val sourceBalance = transactionRepository.getAllByAccount(source).sumOf { it.balance }
        accountRepository.getAccountById(source.id)?.let { acc ->
            acc.balance = sourceBalance
            accountRepository.update(acc)
        }

        val destBalance = transactionRepository.getAllByAccount(destination).sumOf { it.balance }
        accountRepository.getAccountById(destination.id)?.let { acc ->
            acc.balance = destBalance
            accountRepository.update(acc)
        }
    }
}

package application.importStatement.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Category
import domain.entity.Group
import domain.entity.Party
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.AccountType
import domain.enums.PartyType
import domain.enums.TransactionType
import domain.structs.ParsedEntry
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CheckDuplicatesUseCaseTest {

    private val repo = mockk<ITransactionRepository>()
    private val useCase = CheckDuplicatesUseCase(repo)

    private val account = BankAccount(
        id = 1L, type = AccountType.CHECKING, name = "Test",
        icon = "_default.svg", balance = 0.0, position = 1, group = Group()
    )
    private val party = Party(id = 1L, name = "P", type = PartyType.PAYER)

    private fun entry(date: LocalDateTime, balance: Double) = ParsedEntry(
        fitId = null,
        date = date,
        rawCounterpartyName = "Shop",
        description = "Test",
        balance = balance,
        type = TransactionType.EXPENSE,
    )

    private fun tx(date: LocalDateTime, balance: Double) = Transaction(
        party = party, account = account, category = Category(),
        subcategory = null, tags = null,
        date = date, description = "T", balance = balance, type = TransactionType.EXPENSE,
    )

    @Test
    fun `entry matching existing date and balance is marked as duplicate`() {
        val txDate = LocalDateTime.of(2024, 1, 15, 10, 0)
        every { repo.getAllByAccount(account) } returns listOf(tx(txDate, -50.0))

        val result = useCase.execute(listOf(entry(txDate, -50.0)), account)

        assertTrue(result[0].isPossibleDuplicate)
    }

    @Test
    fun `entry with different balance is not marked as duplicate`() {
        val txDate = LocalDateTime.of(2024, 1, 15, 10, 0)
        every { repo.getAllByAccount(account) } returns listOf(tx(txDate, -50.0))

        val result = useCase.execute(listOf(entry(txDate, -99.0)), account)

        assertFalse(result[0].isPossibleDuplicate)
    }

    @Test
    fun `entry with different date is not marked as duplicate`() {
        val txDate = LocalDateTime.of(2024, 1, 15, 10, 0)
        val entryDate = LocalDateTime.of(2024, 1, 16, 10, 0)
        every { repo.getAllByAccount(account) } returns listOf(tx(txDate, -50.0))

        val result = useCase.execute(listOf(entry(entryDate, -50.0)), account)

        assertFalse(result[0].isPossibleDuplicate)
    }

    @Test
    fun `duplicate check ignores time component of date`() {
        val morning = LocalDateTime.of(2024, 1, 15, 9, 0)
        val evening = LocalDateTime.of(2024, 1, 15, 20, 0)
        every { repo.getAllByAccount(account) } returns listOf(tx(morning, -50.0))

        val result = useCase.execute(listOf(entry(evening, -50.0)), account)

        assertTrue(result[0].isPossibleDuplicate)
    }

    @Test
    fun `no existing transactions means no duplicates`() {
        every { repo.getAllByAccount(account) } returns emptyList()
        val entries = listOf(
            entry(LocalDateTime.of(2024, 1, 15, 10, 0), -50.0),
            entry(LocalDateTime.of(2024, 1, 16, 10, 0), -100.0),
        )

        val result = useCase.execute(entries, account)

        assertTrue(result.none { it.isPossibleDuplicate })
    }

    @Test
    fun `original entries are not mutated - result has new copies`() {
        val txDate = LocalDateTime.of(2024, 1, 15, 10, 0)
        every { repo.getAllByAccount(account) } returns listOf(tx(txDate, -50.0))
        val original = entry(txDate, -50.0)

        val result = useCase.execute(listOf(original), account)

        assertFalse(original.isPossibleDuplicate)
        assertTrue(result[0].isPossibleDuplicate)
    }
}

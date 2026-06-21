package application.dashboard.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Category
import domain.entity.Party
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.PartyType
import domain.enums.TransactionType
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FetchTopPartiesUseCaseTest {

    private val repo = mockk<ITransactionRepository>()
    private val useCase = FetchTopPartiesUseCase(repo)

    private val from = LocalDateTime.of(2024, 1, 1, 0, 0)
    private val to = LocalDateTime.of(2024, 1, 31, 23, 59)

    private fun tx(party: Party, balance: Double, type: TransactionType = TransactionType.EXPENSE) =
        Transaction(
            party = party, account = BankAccount(), category = Category(),
            subcategory = null, tags = null,
            date = from, description = "T", balance = balance, type = type,
        )

    @Test
    fun `groups transactions by party and sums absolute balances`() {
        val alice = Party(id = 1L, name = "Alice", type = PartyType.RECEIVER)
        val bob = Party(id = 2L, name = "Bob", type = PartyType.RECEIVER)
        every { repo.getByDateRange(from, to, TransactionType.EXPENSE, true) } returns listOf(
            tx(alice, -100.0), tx(alice, -50.0), tx(bob, -200.0)
        )

        val result = useCase.execute(from, to)

        assertEquals(2, result.size)
        assertEquals(bob, result[0].party)
        assertEquals(200.0, result[0].amount)
        assertEquals(alice, result[1].party)
        assertEquals(150.0, result[1].amount)
    }

    @Test
    fun `respects limit parameter`() {
        val parties = (1..5).map { Party(id = it.toLong(), name = "P$it", type = PartyType.RECEIVER) }
        every { repo.getByDateRange(from, to, TransactionType.EXPENSE, true) } returns
            parties.map { tx(it, -100.0) }

        val result = useCase.execute(from, to, limit = 2)

        assertEquals(2, result.size)
    }

    @Test
    fun `empty transactions returns empty list`() {
        every { repo.getByDateRange(from, to, TransactionType.EXPENSE, true) } returns emptyList()
        val result = useCase.execute(from, to)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `transaction count is correct per party`() {
        val alice = Party(id = 1L, name = "Alice", type = PartyType.RECEIVER)
        every { repo.getByDateRange(from, to, TransactionType.EXPENSE, true) } returns listOf(
            tx(alice, -100.0), tx(alice, -50.0), tx(alice, -30.0)
        )

        val result = useCase.execute(from, to)

        assertEquals(3, result[0].transactionCount)
    }
}

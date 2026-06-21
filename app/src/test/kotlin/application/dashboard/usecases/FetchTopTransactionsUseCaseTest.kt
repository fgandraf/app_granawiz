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

class FetchTopTransactionsUseCaseTest {

    private val repo = mockk<ITransactionRepository>()
    private val useCase = FetchTopTransactionsUseCase(repo)

    private val from = LocalDateTime.of(2024, 1, 1, 0, 0)
    private val to = LocalDateTime.of(2024, 1, 31, 23, 59)
    private val party = Party(id = 1L, name = "Shop", type = PartyType.RECEIVER)

    private fun tx(balance: Double) = Transaction(
        party = party, account = BankAccount(), category = Category(),
        subcategory = null, tags = null,
        date = from, description = "T", balance = balance, type = TransactionType.EXPENSE,
    )

    @Test
    fun `returns transactions sorted by absolute balance descending`() {
        every { repo.getByDateRange(from, to, TransactionType.EXPENSE, true) } returns listOf(
            tx(-50.0), tx(-200.0), tx(-10.0)
        )

        val result = useCase.execute(from, to)

        assertEquals(-200.0, result[0].balance)
        assertEquals(-50.0, result[1].balance)
        assertEquals(-10.0, result[2].balance)
    }

    @Test
    fun `respects limit parameter`() {
        every { repo.getByDateRange(from, to, TransactionType.EXPENSE, true) } returns
            (1..10).map { tx(-it * 10.0) }

        val result = useCase.execute(from, to, limit = 3)

        assertEquals(3, result.size)
    }

    @Test
    fun `empty list returns empty`() {
        every { repo.getByDateRange(from, to, TransactionType.EXPENSE, true) } returns emptyList()
        val result = useCase.execute(from, to)
        assertTrue(result.isEmpty())
    }
}

package application.dashboard.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Category
import domain.entity.Party
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.PartyType
import domain.enums.TransactionType
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FetchMonthlyFlowUseCaseTest {

    private val repo = mockk<ITransactionRepository>()
    private val useCase = FetchMonthlyFlowUseCase(repo)

    private val party = Party(id = 1L, name = "P", type = PartyType.PAYER)

    private fun tx(date: LocalDateTime, balance: Double, type: TransactionType) = Transaction(
        party = party, account = BankAccount(), category = Category(),
        subcategory = null, tags = null,
        date = date, description = "T", balance = balance, type = type,
    )

    @Test
    fun `monthly granularity returns one bucket per month in range`() {
        val from = LocalDateTime.of(2024, 1, 1, 0, 0)
        val to = LocalDateTime.of(2024, 3, 31, 23, 59)
        every { repo.getByDateRange(any(), any()) } returns emptyList()

        val result = useCase.execute(from, to)

        assertEquals(3, result.size)
    }

    @Test
    fun `income and expense are split correctly per bucket`() {
        val from = LocalDateTime.of(2024, 1, 1, 0, 0)
        val to = LocalDateTime.of(2024, 1, 31, 23, 59)
        val janDate = LocalDateTime.of(2024, 1, 15, 10, 0)
        every { repo.getByDateRange(any(), any()) } returns listOf(
            tx(janDate, 500.0, TransactionType.GAIN),
            tx(janDate, -200.0, TransactionType.EXPENSE),
        )

        val result = useCase.execute(from, to)

        assertEquals(1, result.size)
        assertEquals(500.0, result[0].income, 0.01)
        assertEquals(200.0, result[0].expense, 0.01)
    }

    @Test
    fun `yearly granularity groups by year`() {
        val from = LocalDateTime.of(2022, 1, 1, 0, 0)
        val to = LocalDateTime.of(2024, 12, 31, 23, 59)
        every { repo.getByDateRange(any(), any()) } returns emptyList()

        val result = useCase.execute(from, to, granularity = FetchMonthlyFlowUseCase.Granularity.YEAR)

        assertEquals(3, result.size)
        assertEquals("2022", result[0].label)
        assertEquals("2024", result[2].label)
    }

    @Test
    fun `maxBuckets limits number of monthly buckets`() {
        val from = LocalDateTime.of(2020, 1, 1, 0, 0)
        val to = LocalDateTime.of(2024, 12, 31, 23, 59)
        every { repo.getByDateRange(any(), any()) } returns emptyList()

        val result = useCase.execute(from, to, maxBuckets = 6)

        assertEquals(6, result.size)
    }

    @Test
    fun `NEUTRAL transactions are excluded from buckets`() {
        val from = LocalDateTime.of(2024, 1, 1, 0, 0)
        val to = LocalDateTime.of(2024, 1, 31, 23, 59)
        val janDate = LocalDateTime.of(2024, 1, 15, 10, 0)
        every { repo.getByDateRange(any(), any()) } returns listOf(
            tx(janDate, 300.0, TransactionType.NEUTRAL),
        )

        val result = useCase.execute(from, to)

        assertEquals(0.0, result[0].income, 0.01)
        assertEquals(0.0, result[0].expense, 0.01)
    }
}

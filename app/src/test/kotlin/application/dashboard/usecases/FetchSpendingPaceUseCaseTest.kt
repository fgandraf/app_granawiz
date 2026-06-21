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
import java.time.LocalDate
import java.time.LocalDateTime

class FetchSpendingPaceUseCaseTest {

    private val repo = mockk<ITransactionRepository>()
    private val useCase = FetchSpendingPaceUseCase(repo)

    private val party = Party(id = 1L, name = "Shop", type = PartyType.RECEIVER)
    private val today = LocalDate.of(2024, 3, 15)

    private fun expense(date: LocalDateTime, amount: Double) = Transaction(
        party = party, account = BankAccount(), category = Category(),
        subcategory = null, tags = null,
        date = date, description = "T", balance = -amount, type = TransactionType.EXPENSE,
    )

    @Test
    fun `returns null when no baseline transactions exist`() {
        every { repo.getByDateRange(any(), any(), TransactionType.EXPENSE, true) } returns emptyList()
        val result = useCase.execute(today, baselineMonths = 3)
        assertNull(result)
    }

    @Test
    fun `returns non-null result when baseline transactions exist`() {
        val mtdDate = LocalDateTime.of(2024, 3, 10, 10, 0)
        val baselineDate = LocalDateTime.of(2024, 2, 10, 10, 0)

        every { repo.getByDateRange(any(), any(), TransactionType.EXPENSE, true) } answers {
            val from = firstArg<LocalDateTime>()
            if (from.year == 2024 && from.month.value == 3) {
                listOf(expense(mtdDate, 200.0))
            } else {
                listOf(expense(baselineDate, 400.0))
            }
        }

        val result = useCase.execute(today, baselineMonths = 1)

        assertNotNull(result)
    }

    @Test
    fun `percent is 100 when spending matches baseline average`() {
        val mtdDate = LocalDateTime.of(2024, 3, 10, 10, 0)
        val baselineDate = LocalDateTime.of(2024, 2, 10, 10, 0)

        every { repo.getByDateRange(any(), any(), TransactionType.EXPENSE, true) } answers {
            val from = firstArg<LocalDateTime>()
            if (from.year == 2024 && from.month.value == 3) {
                listOf(expense(mtdDate, 300.0))
            } else {
                listOf(expense(baselineDate, 300.0))
            }
        }

        val result = useCase.execute(today, baselineMonths = 1)

        assertNotNull(result)
        assertEquals(100.0, result!!.percentOfAverage, 1.0)
    }

    @Test
    fun `today field in result matches provided date`() {
        val mtdDate = LocalDateTime.of(2024, 3, 10, 10, 0)
        val baselineDate = LocalDateTime.of(2024, 2, 10, 10, 0)

        every { repo.getByDateRange(any(), any(), TransactionType.EXPENSE, true) } answers {
            val from = firstArg<LocalDateTime>()
            if (from.year == 2024 && from.month.value == 3) {
                listOf(expense(mtdDate, 100.0))
            } else {
                listOf(expense(baselineDate, 100.0))
            }
        }

        val result = useCase.execute(today, baselineMonths = 1)

        assertEquals(today, result!!.today)
    }
}

package application.dashboard.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Category
import domain.entity.Party
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.CategoryType
import domain.enums.PartyType
import domain.enums.TransactionType
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FetchCategoryBreakdownUseCaseTest {

    private val repo = mockk<ITransactionRepository>()
    private val useCase = FetchCategoryBreakdownUseCase(repo)

    private val from = LocalDateTime.of(2024, 1, 1, 0, 0)
    private val to = LocalDateTime.of(2024, 1, 31, 23, 59)
    private val party = Party(id = 1L, name = "Shop", type = PartyType.RECEIVER)

    private fun tx(category: Category, balance: Double) = Transaction(
        party = party, account = BankAccount(), category = category,
        subcategory = null, tags = null,
        date = from, description = "T", balance = balance, type = TransactionType.EXPENSE,
    )

    private fun cat(id: Long, name: String) =
        Category(id = id, type = CategoryType.EXPENSE, name = name, icon = "icon.svg")

    @Test
    fun `empty transactions returns empty list`() {
        every { repo.getByDateRange(from, to, TransactionType.EXPENSE) } returns emptyList()
        val result = useCase.execute(from, to)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `groups by category and calculates percentage`() {
        val food = cat(1, "Food")
        val transport = cat(2, "Transport")
        every { repo.getByDateRange(from, to, TransactionType.EXPENSE) } returns listOf(
            tx(food, -80.0), tx(food, -20.0), tx(transport, -100.0)
        )

        val result = useCase.execute(from, to)

        val foodBreakdown = result.first { it.category == food }
        assertEquals(100.0, foodBreakdown.amount)
        assertEquals(50.0, foodBreakdown.percent, 0.01)
    }

    @Test
    fun `results are sorted by amount descending`() {
        val food = cat(1, "Food")
        val transport = cat(2, "Transport")
        every { repo.getByDateRange(from, to, TransactionType.EXPENSE) } returns listOf(
            tx(food, -50.0), tx(transport, -200.0)
        )

        val result = useCase.execute(from, to)

        assertEquals(transport, result[0].category)
        assertEquals(food, result[1].category)
    }

    @Test
    fun `categories beyond limit are merged into Outros bucket`() {
        val cats = (1..8).map { cat(it.toLong(), "Cat$it") }
        every { repo.getByDateRange(from, to, TransactionType.EXPENSE) } returns
            cats.map { tx(it, -100.0) }

        val result = useCase.execute(from, to, limit = 6)

        assertEquals(6, result.size)
        val outros = result.last()
        assertEquals("Outros", outros.category.name)
    }

    @Test
    fun `number of categories within limit returns all`() {
        val cats = (1..4).map { cat(it.toLong(), "Cat$it") }
        every { repo.getByDateRange(from, to, TransactionType.EXPENSE) } returns
            cats.map { tx(it, -100.0) }

        val result = useCase.execute(from, to, limit = 6)

        assertEquals(4, result.size)
        assertTrue(result.none { it.category.name == "Outros" })
    }
}

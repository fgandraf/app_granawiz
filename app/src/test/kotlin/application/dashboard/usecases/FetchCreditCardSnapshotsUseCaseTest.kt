package application.dashboard.usecases

import domain.contracts.IGroupRepository
import domain.contracts.ITransactionRepository
import domain.entity.Category
import domain.entity.Group
import domain.entity.Party
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.entity.account.CreditCardAccount
import domain.enums.PartyType
import domain.enums.TransactionType
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class FetchCreditCardSnapshotsUseCaseTest {

    private val groupRepo = mockk<IGroupRepository>()
    private val txRepo = mockk<ITransactionRepository>()
    private val useCase = FetchCreditCardSnapshotsUseCase(groupRepo, txRepo)

    private val party = Party(id = 1L, name = "Shop", type = PartyType.RECEIVER)
    private val group = Group(id = 1, name = "G", position = 1)

    private fun card(id: Long, closingDay: Int, dueDay: Int, creditLimit: Double = 5000.0) =
        CreditCardAccount(
            id = id, name = "Card", description = "", position = 1,
            icon = "_default.svg", iconSvg = null, balance = 0.0, group = group,
            creditLimit = creditLimit, closingDay = closingDay, dueDay = dueDay,
        )

    private fun expense(accountId: Long, amount: Double, date: LocalDateTime) = Transaction(
        party = party,
        account = BankAccount(id = accountId, type = domain.enums.AccountType.CREDIT_CARD, name = "Card",
            icon = "_default.svg", balance = 0.0, position = 1, group = group),
        category = Category(), subcategory = null, tags = null,
        date = date, description = "T", balance = -amount, type = TransactionType.EXPENSE,
    )

    @Test
    fun `returns empty list when no credit cards exist`() {
        val g = Group(id = 1, name = "G", position = 1)
        g.accounts.add(BankAccount()) // regular checking, not credit card
        every { groupRepo.getAll() } returns listOf(g)
        every { txRepo.getByDateRange(any(), any(), any(), any()) } returns emptyList()

        val result = useCase.execute(LocalDate.of(2024, 3, 15))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `returns one snapshot per credit card`() {
        val c1 = card(id = 1L, closingDay = 10, dueDay = 20)
        val c2 = card(id = 2L, closingDay = 15, dueDay = 25)
        group.accounts.addAll(listOf(c1, c2))
        every { groupRepo.getAll() } returns listOf(group)
        every { txRepo.getByDateRange(any(), any(), any(), any()) } returns emptyList()

        val result = useCase.execute(LocalDate.of(2024, 3, 15))

        assertEquals(2, result.size)
    }

    @Test
    fun `available limit equals credit limit minus invoice`() {
        val c = card(id = 1L, closingDay = 20, dueDay = 28, creditLimit = 1000.0)
        val g = Group(id = 1, name = "G", position = 1)
        g.accounts.add(c)
        every { groupRepo.getAll() } returns listOf(g)

        val txDate = LocalDateTime.of(2024, 3, 10, 10, 0)
        val txWithCard = expense(c.id, 300.0, txDate)
        every { txRepo.getByDateRange(any(), any(), TransactionType.EXPENSE, true) } returns listOf(txWithCard)

        val result = useCase.execute(LocalDate.of(2024, 3, 15))

        val snapshot = result.first { it.account.id == c.id }
        assertEquals(300.0, snapshot.currentInvoice, 0.01)
        assertEquals(700.0, snapshot.availableLimit, 0.01)
    }

    @Test
    fun `due date is this month when due day not yet passed`() {
        val c = card(id = 1L, closingDay = 10, dueDay = 25)
        val g = Group(id = 1, name = "G", position = 1)
        g.accounts.add(c)
        every { groupRepo.getAll() } returns listOf(g)
        every { txRepo.getByDateRange(any(), any(), TransactionType.EXPENSE, true) } returns emptyList()

        val result = useCase.execute(LocalDate.of(2024, 3, 15))

        val snapshot = result.first()
        assertEquals(LocalDate.of(2024, 3, 25), snapshot.nextDueDate)
    }

    @Test
    fun `due date moves to next month when due day already passed`() {
        val c = card(id = 1L, closingDay = 5, dueDay = 10)
        val g = Group(id = 1, name = "G", position = 1)
        g.accounts.add(c)
        every { groupRepo.getAll() } returns listOf(g)
        every { txRepo.getByDateRange(any(), any(), TransactionType.EXPENSE, true) } returns emptyList()

        val result = useCase.execute(LocalDate.of(2024, 3, 15))

        val snapshot = result.first()
        assertEquals(LocalDate.of(2024, 4, 10), snapshot.nextDueDate)
    }
}

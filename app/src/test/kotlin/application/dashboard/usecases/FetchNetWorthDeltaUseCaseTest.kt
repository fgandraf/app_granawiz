package application.dashboard.usecases

import domain.contracts.IGroupRepository
import domain.contracts.ITransactionRepository
import domain.entity.Category
import domain.entity.Group
import domain.entity.Party
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.AccountType
import domain.enums.PartyType
import domain.enums.TransactionType
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class FetchNetWorthDeltaUseCaseTest {

    private val groupRepo = mockk<IGroupRepository>()
    private val txRepo = mockk<ITransactionRepository>()
    private val useCase = FetchNetWorthDeltaUseCase(groupRepo, txRepo)

    private val party = Party(id = 1L, name = "P", type = PartyType.PAYER)
    private val today = LocalDate.of(2024, 3, 15)

    private fun account(balance: Double) = BankAccount(
        id = 1L, type = AccountType.CHECKING, name = "A",
        icon = "_default.svg", balance = balance, position = 1, group = Group()
    )

    private fun tx(balance: Double, type: TransactionType, date: LocalDateTime = LocalDateTime.of(2024, 3, 5, 10, 0)) =
        Transaction(
            party = party, account = account(0.0), category = Category(),
            subcategory = null, tags = null,
            date = date, description = "T", balance = balance, type = type,
        )

    @Test
    fun `total reflects sum of all account balances`() {
        val group = Group(id = 1, name = "G", position = 1)
        group.accounts.add(account(1000.0))
        group.accounts.add(account(500.0))
        every { groupRepo.getAll() } returns listOf(group)
        every { txRepo.getByDateRange(any(), any()) } returns emptyList()

        val result = useCase.execute(today)

        assertEquals(1500.0, result.total)
    }

    @Test
    fun `deltaAmount sums GAIN minus EXPENSE for current month`() {
        val group = Group(id = 1, name = "G", position = 1)
        group.accounts.add(account(1200.0))
        every { groupRepo.getAll() } returns listOf(group)
        every { txRepo.getByDateRange(any(), any()) } returns listOf(
            tx(500.0, TransactionType.GAIN),
            tx(-300.0, TransactionType.EXPENSE),
        )

        val result = useCase.execute(today)

        assertEquals(200.0, result.deltaAmount, 0.01)
    }

    @Test
    fun `NEUTRAL transactions contribute zero to delta`() {
        val group = Group(id = 1, name = "G", position = 1)
        group.accounts.add(account(1000.0))
        every { groupRepo.getAll() } returns listOf(group)
        every { txRepo.getByDateRange(any(), any()) } returns listOf(
            tx(100.0, TransactionType.NEUTRAL),
        )

        val result = useCase.execute(today)

        assertEquals(0.0, result.deltaAmount, 0.01)
    }

    @Test
    fun `delta percent calculated from start of month total`() {
        val group = Group(id = 1, name = "G", position = 1)
        group.accounts.add(account(1200.0))
        every { groupRepo.getAll() } returns listOf(group)
        every { txRepo.getByDateRange(any(), any()) } returns listOf(
            tx(200.0, TransactionType.GAIN),
        )

        val result = useCase.execute(today)

        // startOfMonthTotal = 1200 - 200 = 1000; percent = 200/1000 * 100 = 20%
        assertEquals(20.0, result.deltaPercent, 0.01)
    }

    @Test
    fun `zero start of month total yields zero percent`() {
        val group = Group(id = 1, name = "G", position = 1)
        group.accounts.add(account(100.0))
        every { groupRepo.getAll() } returns listOf(group)
        every { txRepo.getByDateRange(any(), any()) } returns listOf(
            tx(100.0, TransactionType.GAIN),
        )

        val result = useCase.execute(today)

        // startOfMonthTotal = 100 - 100 = 0
        assertEquals(0.0, result.deltaPercent, 0.01)
    }
}

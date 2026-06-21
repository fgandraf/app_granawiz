package application.schedule.usecases

import domain.contracts.IScheduleRepository
import domain.contracts.ITransactionRepository
import domain.entity.Category
import domain.entity.Group
import domain.entity.Party
import domain.entity.Schedule
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.entity.account.CreditCardAccount
import domain.enums.PartyType
import domain.enums.ScheduleFrequency
import domain.enums.TransactionType
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class SaveScheduleUseCaseTest {

    private val scheduleRepo = mockk<IScheduleRepository>(relaxed = true)
    private val txRepo = mockk<ITransactionRepository>(relaxed = true)
    private val useCase = SaveScheduleUseCase(scheduleRepo, txRepo)

    private val party = Party(id = 1L, name = "P", type = PartyType.PAYER)
    private val category = Category()
    private val account = BankAccount()
    private val pastDate = LocalDateTime.of(2020, 1, 15, 10, 0)

    private fun schedule(
        id: Long = 0L,
        frequency: ScheduleFrequency = ScheduleFrequency.ONCE,
        startDate: LocalDateTime = pastDate,
        account: BankAccount = this.account,
    ) = Schedule(
        id = id,
        party = party,
        account = account,
        category = category,
        subcategory = null,
        tags = mutableListOf(),
        startDate = startDate,
        description = "Test",
        balance = -100.0,
        type = TransactionType.EXPENSE,
        frequency = frequency,
    )

    @Test
    fun `existing schedule calls update and does not insert transaction`() {
        val sched = schedule(id = 5L)

        useCase.execute(sched)

        verify { scheduleRepo.update(sched) }
        verify(exactly = 0) { txRepo.insert(any()) }
    }

    @Test
    fun `new ONCE schedule with past date inserts transaction with correct fields`() {
        val sched = schedule(id = 0L, frequency = ScheduleFrequency.ONCE, startDate = pastDate)
        val slot = slot<Transaction>()
        every { txRepo.insert(capture(slot)) } returns Unit

        useCase.execute(sched)

        verify(exactly = 0) { scheduleRepo.update(any()) }
        val tx = slot.captured
        assertEquals(sched.party, tx.party)
        assertEquals(sched.account, tx.account)
        assertEquals(sched.category, tx.category)
        assertEquals(sched.startDate, tx.date)
        assertEquals(sched.balance, tx.balance)
        assertEquals(sched.type, tx.type)
        assertEquals("1/1", tx.installment)
    }

    @Test
    fun `new ONCE past schedule with regular account has null billingYearMonth`() {
        val sched = schedule(id = 0L, frequency = ScheduleFrequency.ONCE, startDate = pastDate)
        val slot = slot<Transaction>()
        every { txRepo.insert(capture(slot)) } returns Unit

        useCase.execute(sched)

        assertNull(slot.captured.billingYearMonth)
    }

    @Test
    fun `new ONCE past schedule with credit card sets billingYearMonth`() {
        val card = CreditCardAccount(
            id = 1L, name = "Card", description = "", position = 1,
            icon = "_default.svg", balance = 0.0, group = Group(),
            creditLimit = 5000.0, closingDay = 20, dueDay = 28,
        )
        val sched = schedule(id = 0L, frequency = ScheduleFrequency.ONCE, startDate = pastDate, account = card)
        val slot = slot<Transaction>()
        every { txRepo.insert(capture(slot)) } returns Unit

        useCase.execute(sched)

        assertNotNull(slot.captured.billingYearMonth)
    }
}

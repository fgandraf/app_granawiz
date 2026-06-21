package application.account

import domain.contracts.IAccountRepository
import domain.contracts.IScheduleRepository
import domain.contracts.ITransactionRepository
import domain.entity.Group
import domain.entity.Schedule
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.AccountType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test

class AccountHandlerDeleteTest {

    private val accountRepo = mockk<IAccountRepository>(relaxed = true)
    private val transactionRepo = mockk<ITransactionRepository>(relaxed = true)
    private val scheduleRepo = mockk<IScheduleRepository>(relaxed = true)

    private val handler = AccountHandler(
        accountRepository = accountRepo,
        transactionRepository = transactionRepo,
        scheduleRepository = scheduleRepo,
        moveAccountPosition = mockk(relaxed = true),
        updateAccountBalance = mockk(relaxed = true),
        saveAccount = mockk(relaxed = true),
    )

    private val account = BankAccount(
        id = 1L, type = AccountType.CHECKING, name = "Conta",
        icon = "_default.svg", balance = 0.0, position = 1, group = Group()
    )

    @Test
    fun `deletes account directly when no transactions or schedules exist`() {
        every { transactionRepo.getAllByAccount(account) } returns emptyList()
        every { scheduleRepo.getAllByAccount(account) } returns emptyList()

        handler.deleteAccount(account)

        verify(exactly = 0) { transactionRepo.delete(any()) }
        verify(exactly = 0) { scheduleRepo.delete(any()) }
        verify { accountRepo.delete(account) }
    }

    @Test
    fun `deletes all transactions before deleting account`() {
        val t1 = Transaction()
        val t2 = Transaction()
        every { transactionRepo.getAllByAccount(account) } returns listOf(t1, t2)
        every { scheduleRepo.getAllByAccount(account) } returns emptyList()

        handler.deleteAccount(account)

        verify { transactionRepo.delete(t1) }
        verify { transactionRepo.delete(t2) }
        verify { accountRepo.delete(account) }
    }

    @Test
    fun `deletes all schedules before deleting account`() {
        val s1 = Schedule()
        val s2 = Schedule()
        every { transactionRepo.getAllByAccount(account) } returns emptyList()
        every { scheduleRepo.getAllByAccount(account) } returns listOf(s1, s2)

        handler.deleteAccount(account)

        verify { scheduleRepo.delete(s1) }
        verify { scheduleRepo.delete(s2) }
        verify { accountRepo.delete(account) }
    }

    @Test
    fun `deletes transactions then schedules then account in correct order`() {
        val tx = Transaction()
        val sc = Schedule()
        every { transactionRepo.getAllByAccount(account) } returns listOf(tx)
        every { scheduleRepo.getAllByAccount(account) } returns listOf(sc)

        handler.deleteAccount(account)

        verifyOrder {
            transactionRepo.delete(tx)
            scheduleRepo.delete(sc)
            accountRepo.delete(account)
        }
    }
}

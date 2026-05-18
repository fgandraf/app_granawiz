package application.account.usecases

import domain.contracts.IAccountRepository
import domain.entity.Group
import domain.entity.account.BankAccount
import domain.enums.AccountType
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UpdateAccountBalanceUseCaseTest {

    private val dao = mockk<IAccountRepository>(relaxed = true)
    private val useCase = UpdateAccountBalanceUseCase(dao)

    private fun account(id: Long, balance: Double = 100.0) = BankAccount(
        id = id, type = AccountType.CHECKING, name = "Test",
        icon = "_default.svg", balance = balance, position = 1, group = Group()
    )

    @Test
    fun `account not found returns without calling update`() {
        every { dao.getAccountById(99L) } returns null
        useCase.execute(99L, 500.0)
        verify(exactly = 0) { dao.update(any()) }
    }

    @Test
    fun `account found sets new balance and calls update`() {
        val acc = account(1L)
        every { dao.getAccountById(1L) } returns acc
        useCase.execute(1L, 999.99)
        assertEquals(999.99, acc.balance)
        verify { dao.update(acc) }
    }

    @Test
    fun `balance can be set to zero`() {
        val acc = account(1L, balance = 500.0)
        every { dao.getAccountById(1L) } returns acc
        useCase.execute(1L, 0.0)
        assertEquals(0.0, acc.balance)
        verify { dao.update(acc) }
    }

    @Test
    fun `balance can be set to negative value`() {
        val acc = account(1L, balance = 100.0)
        every { dao.getAccountById(1L) } returns acc
        useCase.execute(1L, -250.0)
        assertEquals(-250.0, acc.balance)
        verify { dao.update(acc) }
    }
}

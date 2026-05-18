package application.transaction.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Group
import domain.entity.account.BankAccount
import domain.enums.AccountType
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FetchTransactionsUseCaseTest {

    private val repo = mockk<ITransactionRepository>()
    private val useCase = FetchTransactionsUseCase(repo)

    private fun account() = BankAccount(
        id = 1L, type = AccountType.CHECKING, name = "Test",
        icon = "_default.svg", balance = 0.0, position = 1, group = Group()
    )

    @Test
    fun `no account returns all transactions`() {
        every { repo.getAll() } returns listOf(mockk(), mockk(), mockk())
        val result = useCase.execute(account = null)
        assertEquals(3, result.size)
    }

    @Test
    fun `with account filters by account`() {
        val acc = account()
        every { repo.getAllByAccount(acc) } returns listOf(mockk())
        val result = useCase.execute(acc)
        assertEquals(1, result.size)
    }
}

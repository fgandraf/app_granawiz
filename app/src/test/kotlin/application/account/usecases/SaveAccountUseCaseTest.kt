package application.account.usecases

import domain.contracts.IAccountRepository
import domain.entity.Group
import domain.entity.account.BankAccount
import domain.enums.AccountType
import io.mockk.*
import org.junit.jupiter.api.Test

class SaveAccountUseCaseTest {

    private val dao = mockk<IAccountRepository>(relaxed = true)
    private val useCase = SaveAccountUseCase(dao)

    private fun account(id: Long) = BankAccount(
        id = id, type = AccountType.CHECKING, name = "Test",
        icon = "_default.svg", balance = 0.0, position = 1, group = Group()
    )

    @Test
    fun `new account with id 0 calls insert`() {
        useCase.execute(account(0L))
        verify { dao.insert(any()) }
        verify(exactly = 0) { dao.update(any()) }
    }

    @Test
    fun `existing account with positive id calls update`() {
        useCase.execute(account(5L))
        verify { dao.update(any()) }
        verify(exactly = 0) { dao.insert(any()) }
    }
}

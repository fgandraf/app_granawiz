package application.schedule.usecases

import domain.contracts.IScheduleRepository
import domain.entity.Schedule
import domain.entity.Group
import domain.entity.account.BankAccount
import domain.enums.AccountType
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FetchSchedulesUseCaseTest {

    private val repo = mockk<IScheduleRepository>()
    private val useCase = FetchSchedulesUseCase(repo)

    private fun schedule() = Schedule()
    private fun account() = BankAccount(
        id = 1L, type = AccountType.CHECKING, name = "Test",
        icon = "_default.svg", balance = 0.0, position = 1, group = Group()
    )

    @Test
    fun `no account returns all schedules`() {
        val all = listOf(schedule(), schedule())
        every { repo.getAll() } returns all

        val result = useCase.execute(account = null)

        assertEquals(2, result.size)
    }

    @Test
    fun `with account returns filtered schedules`() {
        val acc = account()
        val filtered = listOf(schedule())
        every { repo.getAllByAccount(acc) } returns filtered

        val result = useCase.execute(acc)

        assertEquals(1, result.size)
    }
}

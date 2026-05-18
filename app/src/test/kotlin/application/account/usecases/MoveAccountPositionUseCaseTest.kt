package application.account.usecases

import domain.contracts.IAccountRepository
import domain.entity.Group
import domain.entity.account.BankAccount
import domain.enums.AccountType
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MoveAccountPositionUseCaseTest {

    private val repo = mockk<IAccountRepository>(relaxed = true)
    private val useCase = MoveAccountPositionUseCase(repo)

    private fun groupWithTwoAccounts(): Triple<Group, BankAccount, BankAccount> {
        val group = Group(id = 1, name = "G", position = 1)
        val acc1 = BankAccount(id = 1L, type = AccountType.CHECKING, name = "A1", icon = "_default.svg", balance = 0.0, position = 1, group = group)
        val acc2 = BankAccount(id = 2L, type = AccountType.CHECKING, name = "A2", icon = "_default.svg", balance = 0.0, position = 2, group = group)
        group.accounts.addAll(listOf(acc1, acc2))
        return Triple(group, acc1, acc2)
    }

    @Test
    fun `move first account down swaps positions`() {
        val (group, acc1, acc2) = groupWithTwoAccounts()
        useCase.execute(listOf(group), acc1, direction = 1)
        verify { repo.updateAccountPositions(any()) }
        assertEquals(2, acc1.position)
        assertEquals(1, acc2.position)
    }

    @Test
    fun `move second account up swaps positions`() {
        val (group, acc1, acc2) = groupWithTwoAccounts()
        useCase.execute(listOf(group), acc2, direction = -1)
        verify { repo.updateAccountPositions(any()) }
        assertEquals(1, acc2.position)
        assertEquals(2, acc1.position)
    }

    @Test
    fun `move first account up does nothing`() {
        val (group, acc1, _) = groupWithTwoAccounts()
        useCase.execute(listOf(group), acc1, direction = -1)
        verify(exactly = 0) { repo.updateAccountPositions(any()) }
    }

    @Test
    fun `move last account down does nothing`() {
        val (group, _, acc2) = groupWithTwoAccounts()
        useCase.execute(listOf(group), acc2, direction = 1)
        verify(exactly = 0) { repo.updateAccountPositions(any()) }
    }

    @Test
    fun `account group not in list does nothing`() {
        val otherGroup = Group(id = 99, name = "Other", position = 1)
        val group = Group(id = 1, name = "G", position = 1)
        val acc = BankAccount(id = 1L, type = AccountType.CHECKING, name = "A", icon = "_default.svg", balance = 0.0, position = 1, group = otherGroup)
        group.accounts.add(acc)
        useCase.execute(listOf(group), acc, direction = 1)
        verify(exactly = 0) { repo.updateAccountPositions(any()) }
    }
}

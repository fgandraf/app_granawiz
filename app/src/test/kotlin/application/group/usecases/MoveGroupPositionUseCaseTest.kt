package application.group.usecases

import domain.contracts.IGroupRepository
import domain.entity.Group
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MoveGroupPositionUseCaseTest {

    private val repo = mockk<IGroupRepository>(relaxed = true)
    private val useCase = MoveGroupPositionUseCase(repo)

    private fun twoGroups(): Pair<Group, Group> {
        val g1 = Group(id = 1, name = "G1", position = 1)
        val g2 = Group(id = 2, name = "G2", position = 2)
        return g1 to g2
    }

    @Test
    fun `move first group down swaps positions`() {
        val (g1, g2) = twoGroups()
        useCase.execute(listOf(g1, g2), g1, direction = 1)
        verify { repo.updateGroups(any()) }
        assertEquals(2, g1.position)
        assertEquals(1, g2.position)
    }

    @Test
    fun `move second group up swaps positions`() {
        val (g1, g2) = twoGroups()
        useCase.execute(listOf(g1, g2), g2, direction = -1)
        verify { repo.updateGroups(any()) }
        assertEquals(1, g2.position)
        assertEquals(2, g1.position)
    }

    @Test
    fun `move first group up does nothing`() {
        val (g1, g2) = twoGroups()
        useCase.execute(listOf(g1, g2), g1, direction = -1)
        verify(exactly = 0) { repo.updateGroups(any()) }
    }

    @Test
    fun `move last group down does nothing`() {
        val (g1, g2) = twoGroups()
        useCase.execute(listOf(g1, g2), g2, direction = 1)
        verify(exactly = 0) { repo.updateGroups(any()) }
    }

    @Test
    fun `group not in list does nothing`() {
        val (g1, g2) = twoGroups()
        val outsider = Group(id = 99, name = "X", position = 1)
        useCase.execute(listOf(g1, g2), outsider, direction = 1)
        verify(exactly = 0) { repo.updateGroups(any()) }
    }
}

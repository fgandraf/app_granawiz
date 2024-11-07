package application.group.usecases

import domain.contracts.IGroupRepository
import domain.entity.Group
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AddNewGroupUseCaseTest {

    private val repo = mockk<IGroupRepository>(relaxed = true)
    private val useCase = AddNewGroupUseCase(repo)

    @Test
    fun `new group gets position equal to existing count plus one`() {
        every { repo.getAll() } returns listOf(Group(), Group())
        val slot = slot<Group>()
        every { repo.insert(capture(slot)) } returns Unit

        useCase.execute("Savings")

        assertEquals("Savings", slot.captured.name)
        assertEquals(3, slot.captured.position)
    }

    @Test
    fun `first group gets position 1 when no existing groups`() {
        every { repo.getAll() } returns emptyList()
        val slot = slot<Group>()
        every { repo.insert(capture(slot)) } returns Unit

        useCase.execute("My Group")

        assertEquals(1, slot.captured.position)
        assertEquals("My Group", slot.captured.name)
    }
}

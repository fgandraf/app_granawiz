package application.group.usecases

import domain.contracts.IGroupRepository
import domain.entity.Group
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RenameGroupUseCaseTest {

    private val repo = mockk<IGroupRepository>(relaxed = true)
    private val useCase = RenameGroupUseCase(repo)

    @Test
    fun `creates renamed copy with new name and calls update`() {
        val group = Group(id = 5L, name = "Old Name", position = 3)
        val slot = slot<Group>()
        every { repo.update(capture(slot)) } returns Unit

        useCase.execute(group, "New Name")

        assertEquals("New Name", slot.captured.name)
    }

    @Test
    fun `preserves id and position in renamed group`() {
        val group = Group(id = 5L, name = "Old Name", position = 3)
        val slot = slot<Group>()
        every { repo.update(capture(slot)) } returns Unit

        useCase.execute(group, "New Name")

        assertEquals(5L, slot.captured.id)
        assertEquals(3, slot.captured.position)
    }
}

package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.enums.PartyType
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UpdatePartyUseCaseTest {

    private val repo = mockk<IPartyRepository>(relaxed = true)
    private val useCase = UpdatePartyUseCase(repo)

    private val party = Party(id = 1L, name = "Alice", type = PartyType.PAYER)

    @Test
    fun `returns error when new name already exists in database`() {
        val conflicting = Party(id = 2L, name = "Bob", type = PartyType.PAYER)
        every { repo.getPartyByName("Bob") } returns conflicting

        val (msg, updated) = useCase.execute(party, "Bob")

        assertTrue(msg.contains("Bob"))
        assertNull(updated)
        verify(exactly = 0) { repo.update(any()) }
    }

    @Test
    fun `updates and returns party with new name when available`() {
        every { repo.getPartyByName("Carol") } returns null

        val (_, updated) = useCase.execute(party, "Carol")

        assertNotNull(updated)
        assertEquals("Carol", updated!!.name)
        assertEquals(1L, updated.id)
        assertEquals(PartyType.PAYER, updated.type)
        verify { repo.update(any()) }
    }

    @Test
    fun `success message returned on successful update`() {
        every { repo.getPartyByName(any()) } returns null

        val (msg, _) = useCase.execute(party, "New Name")

        assertTrue(msg.isNotBlank())
    }
}

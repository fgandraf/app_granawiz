package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.entity.PartyName
import domain.enums.PartyType
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AddNameUseCaseTest {

    private val repo = mockk<IPartyRepository>(relaxed = true)
    private val useCase = AddNameUseCase(repo)

    private val party = Party(id = 1L, name = "Alice", type = PartyType.PAYER)

    @Test
    fun `returns error when name already associated with another party`() {
        val otherParty = Party(id = 2L, name = "Bob", type = PartyType.PAYER)
        val existingName = PartyName(id = 5L, name = "Ali", party = otherParty)
        every { repo.getPartyNameByName("Ali") } returns existingName

        val (msg, name) = useCase.execute("Ali", party)

        assertTrue(msg.contains("Ali"))
        assertTrue(msg.contains("Bob"))
        assertNull(name)
        verify(exactly = 0) { repo.insertName(any()) }
    }

    @Test
    fun `inserts and returns new name when not taken`() {
        every { repo.getPartyNameByName("Ali") } returns null

        val (_, name) = useCase.execute("Ali", party)

        assertNotNull(name)
        assertEquals("Ali", name!!.name)
        assertEquals(party, name.party)
        verify { repo.insertName(any()) }
    }

    @Test
    fun `success message returned on successful insertion`() {
        every { repo.getPartyNameByName(any()) } returns null

        val (msg, _) = useCase.execute("Alias", party)

        assertTrue(msg.isNotBlank())
    }
}

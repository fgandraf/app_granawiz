package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.entity.PartyName
import domain.enums.PartyType
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UpdateNameUseCaseTest {

    private val repo = mockk<IPartyRepository>(relaxed = true)
    private val useCase = UpdateNameUseCase(repo)

    private val party = Party(id = 1L, name = "Alice", type = PartyType.PAYER)
    private val partyName = PartyName(id = 5L, name = "Old Alias", party = party)

    @Test
    fun `returns error when name already linked to another party`() {
        val otherParty = Party(id = 2L, name = "Bob", type = PartyType.PAYER)
        val existing = PartyName(id = 99L, name = "Ali", party = otherParty)
        every { repo.getPartyNameByName("Ali") } returns existing

        val (msg, updated) = useCase.execute(partyName, "Ali")

        assertTrue(msg.contains("Ali"))
        assertNull(updated)
    }

    @Test
    fun `returns updated party name when name is available`() {
        every { repo.getPartyNameByName("New Alias") } returns null

        val (_, updated) = useCase.execute(partyName, "New Alias")

        assertNotNull(updated)
        assertEquals("New Alias", updated!!.name)
        assertEquals(5L, updated.id)
        assertEquals(party, updated.party)
    }

    @Test
    fun `success message returned on successful update`() {
        every { repo.getPartyNameByName(any()) } returns null

        val (msg, _) = useCase.execute(partyName, "X")

        assertTrue(msg.isNotBlank())
    }
}

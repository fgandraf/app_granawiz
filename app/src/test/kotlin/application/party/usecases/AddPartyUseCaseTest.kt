package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.enums.PartyType
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AddPartyUseCaseTest {

    private val repo = mockk<IPartyRepository>(relaxed = true)
    private val useCase = AddPartyUseCase(repo)

    @Test
    fun `returns error message when party name already exists`() {
        val existing = Party(id = 1L, name = "Alice", type = PartyType.PAYER)
        every { repo.getPartyByName("Alice") } returns existing

        val (msg, party) = useCase.execute("Alice", PartyType.PAYER)

        assertTrue(msg.contains("Alice"))
        assertNull(party)
        verify(exactly = 0) { repo.insert(any()) }
    }

    @Test
    fun `inserts and returns new party when name is unique`() {
        every { repo.getPartyByName("Bob") } returns null

        val (_, party) = useCase.execute("Bob", PartyType.RECEIVER)

        assertNotNull(party)
        assertEquals("Bob", party!!.name)
        assertEquals(PartyType.RECEIVER, party.type)
        verify { repo.insert(any()) }
    }

    @Test
    fun `success message returned on successful creation`() {
        every { repo.getPartyByName(any()) } returns null

        val (msg, _) = useCase.execute("Carol", PartyType.PAYER)

        assertTrue(msg.isNotBlank())
    }
}

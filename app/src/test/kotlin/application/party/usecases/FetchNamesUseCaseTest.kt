package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.entity.PartyName
import domain.enums.PartyType
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FetchNamesUseCaseTest {

    private val repo = mockk<IPartyRepository>()
    private val useCase = FetchNamesUseCase(repo)

    @Test
    fun `null party returns empty list without calling repo`() {
        val result = useCase.execute(null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `returns names for found party`() {
        val party = Party(id = 1L, name = "Alice", type = PartyType.PAYER)
        val alias = PartyName(id = 10L, name = "Ali", party = party)
        party.partiesNames.add(alias)
        every { repo.getAll(PartyType.PAYER) } returns mutableListOf(party)

        val result = useCase.execute(party)

        assertEquals(1, result.size)
        assertEquals("Ali", result[0].name)
    }

    @Test
    fun `returns empty list when party has no aliases`() {
        val party = Party(id = 1L, name = "Alice", type = PartyType.PAYER)
        every { repo.getAll(PartyType.PAYER) } returns mutableListOf(party)

        val result = useCase.execute(party)

        assertTrue(result.isEmpty())
    }
}

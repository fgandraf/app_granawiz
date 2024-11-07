package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.enums.PartyType
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DeletePartyUseCaseTest {

    private val repo = mockk<IPartyRepository>(relaxed = true)
    private val useCase = DeletePartyUseCase(repo)

    private val party = Party(id = 1L, name = "Alice", type = PartyType.PAYER)

    @Test
    fun `returns error message when party has linked transactions`() {
        every { repo.hasTransactions(party) } returns true

        val result = useCase.execute(party)

        assertNotNull(result)
        assertTrue(result!!.isNotBlank())
        verify(exactly = 0) { repo.delete(any()) }
    }

    @Test
    fun `deletes party and returns null when no transactions linked`() {
        every { repo.hasTransactions(party) } returns false

        val result = useCase.execute(party)

        assertNull(result)
        verify { repo.delete(party) }
    }
}

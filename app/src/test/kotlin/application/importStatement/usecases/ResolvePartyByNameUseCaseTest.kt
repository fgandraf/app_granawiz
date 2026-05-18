package application.importStatement.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.entity.PartyName
import domain.enums.PartyType
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ResolvePartyByNameUseCaseTest {

    private val repo = mockk<IPartyRepository>()
    private val useCase = ResolvePartyByNameUseCase(repo)

    @Test
    fun `empty name returns null without calling repo`() {
        val result = useCase.execute("   ")
        assertNull(result)
    }

    @Test
    fun `blank string returns null`() {
        val result = useCase.execute("")
        assertNull(result)
    }

    @Test
    fun `party name alias is resolved to its parent party`() {
        val parent = Party(id = 1L, name = "Alice", type = PartyType.PAYER)
        val alias = PartyName(id = 10L, name = "Ali", party = parent)
        every { repo.getPartyNameByName("Ali") } returns alias

        val result = useCase.execute("Ali")

        assertEquals(parent, result)
    }

    @Test
    fun `direct party name match returned when no alias exists`() {
        val direct = Party(id = 2L, name = "Bob", type = PartyType.RECEIVER)
        every { repo.getPartyNameByName("Bob") } returns null
        every { repo.getPartyByName("Bob") } returns direct

        val result = useCase.execute("Bob")

        assertEquals(direct, result)
    }

    @Test
    fun `returns null when neither alias nor direct match found`() {
        every { repo.getPartyNameByName("Unknown") } returns null
        every { repo.getPartyByName("Unknown") } returns null

        val result = useCase.execute("Unknown")

        assertNull(result)
    }

    @Test
    fun `trims whitespace before lookup`() {
        val party = Party(id = 1L, name = "Alice", type = PartyType.PAYER)
        every { repo.getPartyNameByName("Alice") } returns null
        every { repo.getPartyByName("Alice") } returns party

        val result = useCase.execute("  Alice  ")

        assertEquals(party, result)
    }
}

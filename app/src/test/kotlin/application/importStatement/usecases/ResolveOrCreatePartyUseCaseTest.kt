package application.importStatement.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.enums.PartyType
import domain.enums.TransactionType
import domain.structs.ParsedEntry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ResolveOrCreatePartyUseCaseTest {

    private val partyRepo = mockk<IPartyRepository>(relaxed = true)
    private val resolveByName = mockk<ResolvePartyByNameUseCase>()
    private val useCase = ResolveOrCreatePartyUseCase(partyRepo, resolveByName)

    private fun entry(
        rawName: String = "Shop",
        type: TransactionType = TransactionType.EXPENSE,
        party: Party? = null,
        customPartyName: String? = null,
    ) = ParsedEntry(
        fitId = null,
        date = LocalDateTime.of(2024, 1, 15, 10, 0),
        rawCounterpartyName = rawName,
        description = "Test",
        balance = -50.0,
        type = type,
        party = party,
        customPartyName = customPartyName,
    )

    @Test
    fun `pre-set party on entry is returned directly`() {
        val existing = Party(id = 1L, name = "Alice", type = PartyType.PAYER)
        val result = useCase.execute(entry(party = existing))
        assertEquals(existing, result)
        verify(exactly = 0) { partyRepo.insert(any()) }
    }

    @Test
    fun `name resolved via alias returns matched party`() {
        val resolved = Party(id = 2L, name = "Bob", type = PartyType.RECEIVER)
        every { resolveByName.execute("Shop") } returns resolved

        val result = useCase.execute(entry(rawName = "Shop"))

        assertEquals(resolved, result)
        verify(exactly = 0) { partyRepo.insert(any()) }
    }

    @Test
    fun `custom party name takes precedence over raw name for lookup`() {
        val resolved = Party(id = 3L, name = "Custom", type = PartyType.RECEIVER)
        every { resolveByName.execute("Custom Name") } returns resolved

        val result = useCase.execute(entry(rawName = "Shop", customPartyName = "Custom Name"))

        assertEquals(resolved, result)
    }

    @Test
    fun `new EXPENSE party is created as RECEIVER when not resolved`() {
        every { resolveByName.execute(any()) } returns null

        val result = useCase.execute(entry(rawName = "New Shop", type = TransactionType.EXPENSE))

        assertEquals(PartyType.RECEIVER, result.type)
        verify { partyRepo.insert(any()) }
    }

    @Test
    fun `new GAIN party is created as PAYER when not resolved`() {
        every { resolveByName.execute(any()) } returns null

        val result = useCase.execute(entry(rawName = "Employer", type = TransactionType.GAIN))

        assertEquals(PartyType.PAYER, result.type)
        verify { partyRepo.insert(any()) }
    }

    @Test
    fun `new party name is trimmed raw name`() {
        every { resolveByName.execute("Shop") } returns null

        val result = useCase.execute(entry(rawName = "  Shop  "))

        assertEquals("Shop", result.name)
    }
}

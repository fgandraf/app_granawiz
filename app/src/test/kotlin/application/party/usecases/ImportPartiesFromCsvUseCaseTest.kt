package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.entity.PartyName
import domain.enums.PartyType
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class ImportPartiesFromCsvUseCaseTest {

    private val repo = mockk<IPartyRepository>(relaxed = true)
    private val useCase = ImportPartiesFromCsvUseCase(repo)

    private fun tempCsv(content: String): File =
        File.createTempFile("granawiz_party_import_", ".csv").also {
            it.writeText(content, Charsets.UTF_8)
            it.deleteOnExit()
        }

    private fun party(id: Long, name: String, type: PartyType = PartyType.PAYER) =
        Party(id = id, name = name, type = type)

    @Test
    fun `empty file beyond header returns zero report`() {
        val file = tempCsv("Nome;Tipo;NomesAlternativos\n")
        every { repo.getPartyByName(any()) } returns null

        val report = useCase.execute(file, PartyType.PAYER)

        assertEquals(0, report.imported)
        assertEquals(0, report.skipped)
        assertEquals(0, report.errors)
    }

    @Test
    fun `header-only file returns zero report`() {
        val file = tempCsv("Nome;Tipo;NomesAlternativos")

        val report = useCase.execute(file, PartyType.PAYER)

        assertEquals(0, report.imported)
    }

    @Test
    fun `new party is inserted and counted as imported`() {
        val file = tempCsv("Nome;Tipo;NomesAlternativos\nAlice;PAYER;\n")
        every { repo.getPartyByName("Alice") } returnsMany listOf(null, party(1L, "Alice"))

        val report = useCase.execute(file, PartyType.PAYER)

        assertEquals(1, report.imported)
        assertEquals(0, report.skipped)
        verify { repo.insert(match { it.name == "Alice" && it.type == PartyType.PAYER }) }
    }

    @Test
    fun `existing party is skipped and not re-inserted`() {
        val existing = party(1L, "Alice")
        val file = tempCsv("Nome;Tipo;NomesAlternativos\nAlice;PAYER;\n")
        every { repo.getPartyByName("Alice") } returns existing

        val report = useCase.execute(file, PartyType.PAYER)

        assertEquals(0, report.imported)
        assertEquals(1, report.skipped)
        verify(exactly = 0) { repo.insert(any()) }
    }

    @Test
    fun `new alternative name added for new party`() {
        val file = tempCsv("Nome;Tipo;NomesAlternativos\nAlice;PAYER;Al\n")
        val freshParty = party(1L, "Alice")
        every { repo.getPartyByName("Alice") } returnsMany listOf(null, freshParty)
        every { repo.getPartyNameByName("Al") } returns null

        useCase.execute(file, PartyType.PAYER)

        verify { repo.insertName(match { it.name == "Al" && it.party == freshParty }) }
    }

    @Test
    fun `new alternative name added even when party already existed`() {
        val existing = party(1L, "Alice")
        val file = tempCsv("Nome;Tipo;NomesAlternativos\nAlice;PAYER;AliceNew\n")
        every { repo.getPartyByName("Alice") } returns existing
        every { repo.getPartyNameByName("AliceNew") } returns null

        useCase.execute(file, PartyType.PAYER)

        verify { repo.insertName(match { it.name == "AliceNew" && it.party == existing }) }
    }

    @Test
    fun `alternative name that already exists is not re-inserted`() {
        val existing = party(1L, "Alice")
        val existingName = PartyName(id = 1L, name = "Al", party = existing)
        val file = tempCsv("Nome;Tipo;NomesAlternativos\nAlice;PAYER;Al\n")
        every { repo.getPartyByName("Alice") } returns existing
        every { repo.getPartyNameByName("Al") } returns existingName

        useCase.execute(file, PartyType.PAYER)

        verify(exactly = 0) { repo.insertName(any()) }
    }

    @Test
    fun `multiple alternative names separated by pipe are all processed`() {
        val file = tempCsv("Nome;Tipo;NomesAlternativos\nAlice;PAYER;Al|Alicia|Ali\n")
        val freshParty = party(1L, "Alice")
        every { repo.getPartyByName("Alice") } returnsMany listOf(null, freshParty)
        every { repo.getPartyNameByName(any()) } returns null

        useCase.execute(file, PartyType.PAYER)

        verify { repo.insertName(match { it.name == "Al" }) }
        verify { repo.insertName(match { it.name == "Alicia" }) }
        verify { repo.insertName(match { it.name == "Ali" }) }
    }

    @Test
    fun `multiple parties in file all processed correctly`() {
        val file = tempCsv(
            "Nome;Tipo;NomesAlternativos\n" +
            "Alice;PAYER;\n" +
            "Bob;PAYER;\n" +
            "Carol;PAYER;\n"
        )
        every { repo.getPartyByName(any()) } returnsMany listOf(
            null, party(1L, "Alice"),
            null, party(2L, "Bob"),
            null, party(3L, "Carol"),
        )

        val report = useCase.execute(file, PartyType.PAYER)

        assertEquals(3, report.imported)
        assertEquals(0, report.skipped)
    }

    @Test
    fun `mix of new and existing parties counted correctly`() {
        val existing = party(1L, "Alice")
        val file = tempCsv(
            "Nome;Tipo;NomesAlternativos\n" +
            "Alice;PAYER;\n" +
            "Bob;PAYER;\n"
        )
        every { repo.getPartyByName("Alice") } returns existing
        every { repo.getPartyByName("Bob") } returnsMany listOf(null, party(2L, "Bob"))

        val report = useCase.execute(file, PartyType.PAYER)

        assertEquals(1, report.imported)
        assertEquals(1, report.skipped)
    }

    @Test
    fun `blank lines in file are skipped without error`() {
        val file = tempCsv("Nome;Tipo;NomesAlternativos\n\nAlice;PAYER;\n\n")
        every { repo.getPartyByName("Alice") } returnsMany listOf(null, party(1L, "Alice"))

        val report = useCase.execute(file, PartyType.PAYER)

        assertEquals(1, report.imported)
        assertEquals(0, report.errors)
    }

    @Test
    fun `party type from file is ignored in favour of the type parameter`() {
        val file = tempCsv("Nome;Tipo;NomesAlternativos\nAlice;RECEIVER;\n")
        every { repo.getPartyByName("Alice") } returnsMany listOf(null, party(1L, "Alice", PartyType.PAYER))

        useCase.execute(file, PartyType.PAYER)

        verify { repo.insert(match { it.type == PartyType.PAYER }) }
    }

    @Test
    fun `quoted field with semicolon is parsed as single value`() {
        val file = tempCsv("Nome;Tipo;NomesAlternativos\n\"Alice;Corp\";PAYER;\n")
        every { repo.getPartyByName("Alice;Corp") } returnsMany listOf(null, party(1L, "Alice;Corp"))

        val report = useCase.execute(file, PartyType.PAYER)

        assertEquals(1, report.imported)
        verify { repo.insert(match { it.name == "Alice;Corp" }) }
    }
}

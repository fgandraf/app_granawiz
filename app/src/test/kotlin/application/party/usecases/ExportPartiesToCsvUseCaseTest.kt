package application.party.usecases

import domain.entity.Party
import domain.entity.PartyName
import domain.enums.PartyType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class ExportPartiesToCsvUseCaseTest {

    private val useCase = ExportPartiesToCsvUseCase()

    private fun tempFile() = File.createTempFile("granawiz_party_export_", ".csv").also { it.deleteOnExit() }

    private fun party(name: String, type: PartyType = PartyType.PAYER, names: List<String> = emptyList()): Party {
        val p = Party(id = 1L, name = name, type = type)
        names.forEach { p.partiesNames.add(PartyName(name = it, party = p)) }
        return p
    }

    @Test
    fun `csv has correct headers on first line`() {
        val file = tempFile()
        useCase.execute(emptyList(), file)
        val header = file.readLines(Charsets.UTF_8).first().split(';')
        assertEquals("Nome", header[0])
        assertEquals("Tipo", header[1])
        assertEquals("NomesAlternativos", header[2])
    }

    @Test
    fun `empty list produces only header row`() {
        val file = tempFile()
        useCase.execute(emptyList(), file)
        val lines = file.readLines(Charsets.UTF_8).filter { it.isNotBlank() }
        assertEquals(1, lines.size)
    }

    @Test
    fun `party name and type written correctly`() {
        val file = tempFile()
        useCase.execute(listOf(party("Alice", PartyType.RECEIVER)), file)
        val data = file.readLines(Charsets.UTF_8)[1].split(';')
        assertEquals("Alice", data[0])
        assertEquals("RECEIVER", data[1])
    }

    @Test
    fun `party with no alternative names has empty third column`() {
        val file = tempFile()
        useCase.execute(listOf(party("Alice")), file)
        val data = file.readLines(Charsets.UTF_8)[1].split(';')
        assertEquals("", data[2])
    }

    @Test
    fun `single alternative name written in third column`() {
        val file = tempFile()
        useCase.execute(listOf(party("Alice", names = listOf("Al"))), file)
        val data = file.readLines(Charsets.UTF_8)[1].split(';')
        assertEquals("Al", data[2])
    }

    @Test
    fun `multiple alternative names joined by pipe`() {
        val file = tempFile()
        useCase.execute(listOf(party("Alice", names = listOf("Al", "Alicia"))), file)
        val data = file.readLines(Charsets.UTF_8)[1].split(';')
        assertEquals("Al|Alicia", data[2])
    }

    @Test
    fun `parties sorted alphabetically by name`() {
        val file = tempFile()
        useCase.execute(listOf(party("Zara"), party("Alice"), party("Bob")), file)
        val lines = file.readLines(Charsets.UTF_8).drop(1)
        assertEquals("Alice", lines[0].split(';')[0])
        assertEquals("Bob", lines[1].split(';')[0])
        assertEquals("Zara", lines[2].split(';')[0])
    }

    @Test
    fun `name containing semicolon is quoted`() {
        val file = tempFile()
        useCase.execute(listOf(party("A;B")), file)
        val content = file.readText(Charsets.UTF_8)
        assertTrue(content.contains("\"A;B\""))
    }

    @Test
    fun `name containing double quote is escaped`() {
        val file = tempFile()
        useCase.execute(listOf(party("Say \"Hi\"")), file)
        val content = file.readText(Charsets.UTF_8)
        assertTrue(content.contains("\"Say \"\"Hi\"\"\""))
    }

    @Test
    fun `multiple parties produce correct number of data rows`() {
        val file = tempFile()
        useCase.execute(listOf(party("Alice"), party("Bob"), party("Carol")), file)
        val dataLines = file.readLines(Charsets.UTF_8).filter { it.isNotBlank() }.drop(1)
        assertEquals(3, dataLines.size)
    }

    @Test
    fun `file written with UTF-8 encoding`() {
        val file = tempFile()
        useCase.execute(listOf(party("José")), file)
        val content = file.readText(Charsets.UTF_8)
        assertTrue(content.contains("José"))
    }
}

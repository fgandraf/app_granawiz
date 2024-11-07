package application.transaction.usecases

import domain.entity.Category
import domain.entity.Party
import domain.entity.Subcategory
import domain.entity.Tag
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.CategoryType
import domain.enums.PartyType
import domain.enums.TransactionType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime

class ExportTransactionsToCsvUseCaseTest {

    private val useCase = ExportTransactionsToCsvUseCase()

    private val party = Party(id = 1L, name = "Alice", type = PartyType.PAYER)
    private val category = Category(id = 1L, type = CategoryType.EXPENSE, name = "Food", icon = "food.svg")
    private val account = BankAccount()

    private fun tx(
        balance: Double,
        type: TransactionType = TransactionType.EXPENSE,
        date: LocalDateTime = LocalDateTime.of(2024, 1, 15, 10, 0),
        description: String = "Test",
        subcategory: Subcategory? = null,
        tags: MutableList<Tag>? = null,
        installment: String = "1/1",
    ) = Transaction(
        party = party,
        account = account,
        category = category,
        subcategory = subcategory,
        tags = tags,
        date = date,
        description = description,
        balance = balance,
        type = type,
        installment = installment,
    )

    private fun tempFile() = File.createTempFile("granawiz_test_", ".csv").also { it.deleteOnExit() }

    private fun readCsv(file: File): List<List<String>> =
        file.readLines(Charsets.UTF_8).map { it.split(';') }

    @Test
    fun `csv has correct headers on first line`() {
        val file = tempFile()
        useCase.execute(emptyList(), file)
        val lines = readCsv(file)
        assertEquals("Data", lines[0][0])
        assertEquals("Descricao", lines[0][1])
        assertEquals("Tipo", lines[0][3])
        assertEquals("Valor", lines[0][4])
    }

    @Test
    fun `EXPENSE type maps to Debito`() {
        val file = tempFile()
        useCase.execute(listOf(tx(-50.0, TransactionType.EXPENSE)), file)
        val data = readCsv(file)[1]
        assertEquals("Debito", data[3])
    }

    @Test
    fun `GAIN type maps to Credito`() {
        val file = tempFile()
        useCase.execute(listOf(tx(100.0, TransactionType.GAIN)), file)
        val data = readCsv(file)[1]
        assertEquals("Credito", data[3])
    }

    @Test
    fun `NEUTRAL type maps to Credito`() {
        val file = tempFile()
        useCase.execute(listOf(tx(0.0, TransactionType.NEUTRAL)), file)
        val data = readCsv(file)[1]
        assertEquals("Credito", data[3])
    }

    @Test
    fun `transactions are sorted by date ascending`() {
        val mar = tx(10.0, date = LocalDateTime.of(2024, 3, 1, 0, 0), description = "March")
        val jan = tx(20.0, date = LocalDateTime.of(2024, 1, 1, 0, 0), description = "January")
        val file = tempFile()
        useCase.execute(listOf(mar, jan), file)
        val lines = readCsv(file)
        assertEquals("January", lines[1][1])
        assertEquals("March", lines[2][1])
    }

    @Test
    fun `subcategory appended to category with slash`() {
        val sub = Subcategory(id = 1L, name = "Fast Food", category = category)
        val file = tempFile()
        useCase.execute(listOf(tx(-30.0, subcategory = sub)), file)
        val data = readCsv(file)[1]
        assertEquals("Food/Fast Food", data[5])
    }

    @Test
    fun `category without subcategory shows category name only`() {
        val file = tempFile()
        useCase.execute(listOf(tx(-30.0, subcategory = null)), file)
        val data = readCsv(file)[1]
        assertEquals("Food", data[5])
    }

    @Test
    fun `fields containing semicolon are quoted`() {
        val file = tempFile()
        useCase.execute(listOf(tx(-30.0, description = "a;b")), file)
        val content = file.readText()
        assertTrue(content.contains("\"a;b\""))
    }

    @Test
    fun `empty transaction list produces only header row`() {
        val file = tempFile()
        useCase.execute(emptyList(), file)
        val lines = file.readLines().filter { it.isNotBlank() }
        assertEquals(1, lines.size)
    }

    @Test
    fun `tags joined with comma separator`() {
        val tag1 = Tag(id = 1L, name = "work")
        val tag2 = Tag(id = 2L, name = "monthly")
        val file = tempFile()
        useCase.execute(listOf(tx(-30.0, tags = mutableListOf(tag1, tag2))), file)
        // field contains comma so it gets quoted in the CSV; check raw content
        val content = file.readText()
        assertTrue(content.contains("\"work, monthly\""))
    }
}

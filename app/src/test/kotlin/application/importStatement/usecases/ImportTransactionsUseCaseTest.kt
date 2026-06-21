package application.importStatement.usecases

import application.importStatement.LogLevel
import application.transaction.TransactionHandler
import domain.entity.Category
import domain.entity.Party
import domain.enums.PartyType
import domain.enums.TransactionType
import domain.structs.ParsedEntry
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ImportTransactionsUseCaseTest {

    private val handler = mockk<TransactionHandler>(relaxed = true)
    private val resolveParty = mockk<ResolveOrCreatePartyUseCase>()
    private val resolveCategory = mockk<ResolveOrCreateCategoryUseCase>()
    private val resolveTags = mockk<ResolveOrCreateTagsUseCase>()
    private val useCase = ImportTransactionsUseCase(handler, resolveParty, resolveCategory, resolveTags)

    private val date = LocalDateTime.of(2024, 3, 10, 10, 0)
    private val party = Party(id = 1L, name = "Shop", type = PartyType.RECEIVER)
    private val resolved = ResolveOrCreateCategoryUseCase.Resolved(Category(), null)

    private fun entry(name: String = "Shop") = ParsedEntry(
        fitId = null,
        date = date,
        rawCounterpartyName = name,
        description = name,
        balance = -100.0,
        type = TransactionType.EXPENSE,
    )

    private fun setupHappyPath() {
        every { resolveParty.execute(any()) } returns party
        every { resolveCategory.execute(any()) } returns resolved
        every { resolveTags.execute(any()) } returns mutableListOf()
    }

    @Test
    fun `all entries succeed returns correct imported count`() {
        setupHappyPath()
        val entries = listOf(entry("A"), entry("B"), entry("C"))

        val report = useCase.execute(entries, mockk(relaxed = true)) { _, _ -> }

        assertEquals(3, report.imported)
        assertEquals(0, report.failed)
    }

    @Test
    fun `empty entries returns zero counts`() {
        val report = useCase.execute(emptyList(), mockk(relaxed = true)) { _, _ -> }

        assertEquals(0, report.imported)
        assertEquals(0, report.failed)
    }

    @Test
    fun `exception on one entry increments failed and continues`() {
        every { resolveParty.execute(any()) } throws RuntimeException("boom") andThen party
        every { resolveCategory.execute(any()) } returns resolved
        every { resolveTags.execute(any()) } returns mutableListOf()

        val report = useCase.execute(listOf(entry("Bad"), entry("Good")), mockk(relaxed = true)) { _, _ -> }

        assertEquals(1, report.imported)
        assertEquals(1, report.failed)
    }

    @Test
    fun `all entries fail returns zero imported`() {
        every { resolveParty.execute(any()) } throws RuntimeException("always fails")

        val report = useCase.execute(listOf(entry(), entry()), mockk(relaxed = true)) { _, _ -> }

        assertEquals(0, report.imported)
        assertEquals(2, report.failed)
    }

    @Test
    fun `onLog called with OK on successful entry`() {
        setupHappyPath()
        val logs = mutableListOf<Pair<String, LogLevel>>()

        useCase.execute(listOf(entry()), mockk(relaxed = true)) { msg, level -> logs.add(msg to level) }

        assertTrue(logs.any { it.second == LogLevel.OK })
    }

    @Test
    fun `onLog called with ERROR on failed entry`() {
        every { resolveParty.execute(any()) } throws RuntimeException("fail")
        val logs = mutableListOf<Pair<String, LogLevel>>()

        useCase.execute(listOf(entry()), mockk(relaxed = true)) { msg, level -> logs.add(msg to level) }

        assertTrue(logs.any { it.second == LogLevel.ERROR })
    }

    @Test
    fun `log label includes entry index and total`() {
        setupHappyPath()
        val logs = mutableListOf<String>()

        useCase.execute(listOf(entry(), entry()), mockk(relaxed = true)) { msg, _ -> logs.add(msg) }

        assertTrue(logs.any { it.startsWith("[1/2]") })
        assertTrue(logs.any { it.startsWith("[2/2]") })
    }
}

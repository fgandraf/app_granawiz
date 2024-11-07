package application.importStatement.usecases

import domain.contracts.ITagRepository
import domain.entity.Tag
import domain.enums.TransactionType
import domain.structs.ParsedEntry
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ResolveOrCreateTagsUseCaseTest {

    private val repo = mockk<ITagRepository>(relaxed = true)
    private val useCase = ResolveOrCreateTagsUseCase(repo)

    private fun entry(customTagsText: String?) = ParsedEntry(
        fitId = null,
        date = LocalDateTime.of(2024, 1, 15, 10, 0),
        rawCounterpartyName = "Shop",
        description = "Test",
        balance = -50.0,
        type = TransactionType.EXPENSE,
        customTagsText = customTagsText,
    )

    @Test
    fun `null tags text returns empty list`() {
        val result = useCase.execute(entry(null))
        assertTrue(result.isEmpty())
    }

    @Test
    fun `blank tags text returns empty list`() {
        val result = useCase.execute(entry("   "))
        assertTrue(result.isEmpty())
    }

    @Test
    fun `existing tag is reused from repo without insert`() {
        val existing = Tag(id = 1L, name = "work")
        every { repo.getAll() } returns listOf(existing)

        val result = useCase.execute(entry("work"))

        assertEquals(1, result.size)
        assertEquals(existing, result[0])
        verify(exactly = 0) { repo.insert(any()) }
    }

    @Test
    fun `unknown tag is created and inserted`() {
        every { repo.getAll() } returns emptyList()

        val result = useCase.execute(entry("newTag"))

        assertEquals(1, result.size)
        assertEquals("newTag", result[0].name)
        verify { repo.insert(any()) }
    }

    @Test
    fun `multiple comma separated tags are all resolved`() {
        every { repo.getAll() } returns emptyList()

        val result = useCase.execute(entry("work, monthly, urgent"))

        assertEquals(3, result.size)
    }

    @Test
    fun `tag lookup is case insensitive`() {
        val existing = Tag(id = 1L, name = "Work")
        every { repo.getAll() } returns listOf(existing)

        val result = useCase.execute(entry("work"))

        assertEquals(existing, result[0])
        verify(exactly = 0) { repo.insert(any()) }
    }

    @Test
    fun `duplicate tag names in text are deduplicated`() {
        every { repo.getAll() } returns emptyList()

        val result = useCase.execute(entry("work, Work, WORK"))

        assertEquals(1, result.size)
    }

    @Test
    fun `mixed existing and new tags are handled correctly`() {
        val existing = Tag(id = 1L, name = "work")
        every { repo.getAll() } returns listOf(existing)

        val result = useCase.execute(entry("work, newTag"))

        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "work" })
        assertTrue(result.any { it.name == "newTag" })
        verify(exactly = 1) { repo.insert(any()) }
    }
}

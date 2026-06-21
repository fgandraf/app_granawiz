package application.importStatement.usecases

import application.category.CategoryHandler
import domain.entity.Category
import domain.entity.Subcategory
import domain.enums.CategoryType
import domain.enums.TransactionType
import domain.structs.ParsedEntry
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ResolveOrCreateCategoryUseCaseTest {

    private val handler = mockk<CategoryHandler>()
    private val useCase = ResolveOrCreateCategoryUseCase(handler)

    private val date = LocalDateTime.of(2024, 1, 1, 0, 0)

    private fun entry(
        type: TransactionType = TransactionType.EXPENSE,
        category: Category? = null,
        customCategoryText: String? = null,
    ) = ParsedEntry(
        fitId = null,
        date = date,
        rawCounterpartyName = "Shop",
        description = "Shop",
        balance = -100.0,
        type = type,
        category = category,
        customCategoryText = customCategoryText,
    )

    @Test
    fun `entry with pre-set category returns it directly without calling handler`() {
        val cat = Category(id = 1, type = CategoryType.EXPENSE, name = "Food", icon = "")
        val sub = Subcategory(id = 1, name = "Fast Food", category = cat)

        val result = useCase.execute(entry(category = cat).copy(subcategory = sub))

        assertEquals(cat, result.category)
        assertEquals(sub, result.subcategory)
        verify(exactly = 0) { handler.fetchCategories(any()) }
        verify(exactly = 0) { handler.addCategory(any()) }
    }

    @Test
    fun `EXPENSE type with no text returns Sem categoria in EXPENSE`() {
        every { handler.fetchCategories(CategoryType.EXPENSE) } returns emptyList()
        every { handler.addCategory(any()) } returns Unit

        val result = useCase.execute(entry(type = TransactionType.EXPENSE))

        assertEquals("Sem categoria", result.category.name)
        assertEquals(CategoryType.EXPENSE, result.category.type)
        assertNull(result.subcategory)
    }

    @Test
    fun `GAIN type with no text returns Sem categoria in INCOME`() {
        every { handler.fetchCategories(CategoryType.INCOME) } returns emptyList()
        every { handler.addCategory(any()) } returns Unit

        val result = useCase.execute(entry(type = TransactionType.GAIN))

        assertEquals("Sem categoria", result.category.name)
        assertEquals(CategoryType.INCOME, result.category.type)
    }

    @Test
    fun `Sem categoria already cached is reused without a second addCategory call`() {
        every { handler.fetchCategories(CategoryType.EXPENSE) } returns emptyList()
        every { handler.addCategory(any()) } returns Unit

        useCase.execute(entry(type = TransactionType.EXPENSE))
        useCase.execute(entry(type = TransactionType.EXPENSE))

        verify(exactly = 1) { handler.addCategory(any()) }
    }

    @Test
    fun `text without slash creates new category`() {
        every { handler.fetchCategories(CategoryType.EXPENSE) } returns emptyList()
        every { handler.addCategory(any()) } returns Unit

        val result = useCase.execute(entry(customCategoryText = "Alimentação"))

        assertEquals("Alimentação", result.category.name)
        assertNull(result.subcategory)
        verify { handler.addCategory(match { it.name == "Alimentação" }) }
    }

    @Test
    fun `text with slash creates category and subcategory`() {
        every { handler.fetchCategories(CategoryType.EXPENSE) } returns emptyList()
        every { handler.addCategory(any()) } returns Unit
        every { handler.addSubcategory(any()) } returns Unit

        val result = useCase.execute(entry(customCategoryText = "Alimentação/Restaurante"))

        assertEquals("Alimentação", result.category.name)
        assertNotNull(result.subcategory)
        assertEquals("Restaurante", result.subcategory!!.name)
    }

    @Test
    fun `existing category matched case-insensitively is reused`() {
        val existing = Category(id = 5, type = CategoryType.EXPENSE, name = "Food", icon = "")
        every { handler.fetchCategories(CategoryType.EXPENSE) } returns listOf(existing)

        val result = useCase.execute(entry(customCategoryText = "FOOD"))

        assertEquals(existing, result.category)
        verify(exactly = 0) { handler.addCategory(any()) }
    }

    @Test
    fun `existing subcategory matched case-insensitively is reused`() {
        val sub = Subcategory(id = 2, name = "Fast Food", category = Category())
        val existing = Category(id = 5, type = CategoryType.EXPENSE, name = "Food", icon = "").also {
            it.subcategories.add(sub)
        }
        every { handler.fetchCategories(CategoryType.EXPENSE) } returns listOf(existing)

        val result = useCase.execute(entry(customCategoryText = "food/FAST FOOD"))

        assertEquals(sub, result.subcategory)
        verify(exactly = 0) { handler.addSubcategory(any()) }
    }

    @Test
    fun `fetchCategories called only once per type across multiple execute calls`() {
        every { handler.fetchCategories(CategoryType.EXPENSE) } returns emptyList()
        every { handler.addCategory(any()) } returns Unit

        repeat(3) { useCase.execute(entry(customCategoryText = "Cat$it")) }

        verify(exactly = 1) { handler.fetchCategories(CategoryType.EXPENSE) }
    }
}

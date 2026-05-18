package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.entity.Category
import domain.enums.CategoryType
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UpdateCategoryUseCaseTest {

    private val repo = mockk<ICategoryRepository>(relaxed = true)
    private val useCase = UpdateCategoryUseCase(repo)

    private val category = Category(id = 1L, type = CategoryType.EXPENSE, name = "Food", icon = "food.svg")

    private fun captureUpdated(): Category {
        val slot = slot<Category>()
        every { repo.update(capture(slot)) } returns Unit
        return slot.captured
    }

    @Test
    fun `overrides name when provided`() {
        val slot = slot<Category>()
        every { repo.update(capture(slot)) } returns Unit
        useCase.execute(category, name = "Groceries")
        assertEquals("Groceries", slot.captured.name)
        assertEquals("food.svg", slot.captured.icon)
    }

    @Test
    fun `keeps original name when null provided`() {
        val slot = slot<Category>()
        every { repo.update(capture(slot)) } returns Unit
        useCase.execute(category, name = null)
        assertEquals("Food", slot.captured.name)
    }

    @Test
    fun `overrides icon when provided`() {
        val slot = slot<Category>()
        every { repo.update(capture(slot)) } returns Unit
        useCase.execute(category, icon = "new-icon.svg")
        assertEquals("new-icon.svg", slot.captured.icon)
    }

    @Test
    fun `keeps original icon when null provided`() {
        val slot = slot<Category>()
        every { repo.update(capture(slot)) } returns Unit
        useCase.execute(category, icon = null)
        assertEquals("food.svg", slot.captured.icon)
    }

    @Test
    fun `preserves id and type in updated category`() {
        val slot = slot<Category>()
        every { repo.update(capture(slot)) } returns Unit
        useCase.execute(category, name = "New Name", icon = "new.svg")
        assertEquals(1L, slot.captured.id)
        assertEquals(CategoryType.EXPENSE, slot.captured.type)
    }
}

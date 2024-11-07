package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.entity.Category
import domain.entity.Subcategory
import domain.enums.CategoryType
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UpdateSubcategoryUseCaseTest {

    private val repo = mockk<ICategoryRepository>(relaxed = true)
    private val useCase = UpdateSubcategoryUseCase(repo)

    private val category = Category(id = 1L, type = CategoryType.EXPENSE, name = "Food", icon = "food.svg")
    private val subcategory = Subcategory(id = 10L, name = "Restaurants", category = category)

    @Test
    fun `updates subcategory name and calls updateSubcategory`() {
        val slot = slot<Subcategory>()
        every { repo.updateSubcategory(capture(slot)) } returns Unit
        useCase.execute(subcategory, "Fast Food")
        assertEquals("Fast Food", slot.captured.name)
    }

    @Test
    fun `preserves id and category in updated subcategory`() {
        val slot = slot<Subcategory>()
        every { repo.updateSubcategory(capture(slot)) } returns Unit
        useCase.execute(subcategory, "Fast Food")
        assertEquals(10L, slot.captured.id)
        assertEquals(category, slot.captured.category)
    }
}

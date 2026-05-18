package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.entity.Category
import domain.entity.Subcategory
import domain.enums.CategoryType
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FetchSubcategoriesUseCaseTest {

    private val repo = mockk<ICategoryRepository>()
    private val useCase = FetchSubcategoriesUseCase(repo)

    private val category = Category(id = 1L, type = CategoryType.EXPENSE, name = "Food", icon = "food.svg")

    @Test
    fun `returns subcategories for matching category`() {
        val sub1 = Subcategory(id = 1L, name = "Restaurants", category = category)
        val sub2 = Subcategory(id = 2L, name = "Groceries", category = category)
        category.subcategories.addAll(listOf(sub1, sub2))
        every { repo.getAll(CategoryType.EXPENSE) } returns listOf(category)

        val result = useCase.execute(category)

        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Restaurants" })
        assertTrue(result.any { it.name == "Groceries" })
    }

    @Test
    fun `returns empty list when category not found`() {
        val other = Category(id = 999L, type = CategoryType.EXPENSE, name = "Other", icon = "other.svg")
        every { repo.getAll(CategoryType.EXPENSE) } returns listOf(other)

        val result = useCase.execute(category)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `returns empty list when category has no subcategories`() {
        every { repo.getAll(CategoryType.EXPENSE) } returns listOf(category)

        val result = useCase.execute(category)

        assertTrue(result.isEmpty())
    }
}

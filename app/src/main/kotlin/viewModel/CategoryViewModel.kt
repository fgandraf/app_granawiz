package viewModel

import core.entity.Category
import core.entity.Subcategory
import core.enums.CategoryType
import kotlinx.coroutines.flow.MutableStateFlow
import service.CategoryService

class CategoryViewModel {

    val service: CategoryService = CategoryService()

    val categories = service.categories
    val subCategories = service.subCategories

    val selectedCategory = MutableStateFlow(Category())
    fun selectCategory(category: Category) {
        selectedCategory.value = category
    }

    val selectedSubcategory = MutableStateFlow<Subcategory?>(null)
    val selectedType = MutableStateFlow(CategoryType.INCOME)
}
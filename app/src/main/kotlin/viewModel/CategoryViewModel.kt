package viewModel

import androidx.compose.runtime.*
import core.entity.Category
import core.entity.Subcategory
import core.enums.CategoryType
import kotlinx.coroutines.flow.MutableStateFlow
import service.CategoryService

class CategoryViewModel {

    val service: CategoryService = CategoryService()

    val categories = derivedStateOf { service.categories }
    val subCategories = derivedStateOf { service.subCategories }

    val selectedCategory = MutableStateFlow(Category())
    val selectedSubcategory = MutableStateFlow<Subcategory?>(null)
    val selectedType = MutableStateFlow(CategoryType.INCOME)
}
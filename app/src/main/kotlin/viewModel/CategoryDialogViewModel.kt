package viewModel

import androidx.compose.runtime.derivedStateOf
import core.entity.Category
import core.entity.Subcategory
import kotlinx.coroutines.flow.MutableStateFlow
import service.CategoryService

class CategoryDialogViewModel {

    val service: CategoryService = CategoryService()

    val categories = derivedStateOf { service.categories }
    val subCategories = derivedStateOf { service.subCategories }

    val selectedCategory = MutableStateFlow(Category())
    val selectedSubcategory = MutableStateFlow<Subcategory?>(null)

}
package viewModel

import core.entity.Category
import core.entity.Subcategory
import core.enums.CategoryType
import domain.category.CategoryHandler
import kotlinx.coroutines.flow.MutableStateFlow

class CategoryViewModel(private val handler: CategoryHandler = CategoryHandler()) {


    val categories = MutableStateFlow(emptyList<Category>())
    fun getCategories(type: CategoryType) { categories.value = handler.fetchCategories(type) }

    val subcategories = MutableStateFlow(emptyList<Subcategory>())
    fun getSubcategories(category: Category) { subcategories.value =  handler.fetchSubcategories(category) }

    val selectedCategory = MutableStateFlow(Category())
    val selectedSubcategory = MutableStateFlow<Subcategory?>(null)
    val selectedType = MutableStateFlow(CategoryType.INCOME)

    fun deleteCategory(category: Category) {
        handler.deleteCategory(category)
        getCategories(category.type)
    }

    fun deleteSubcategory(subcategory: Subcategory) {
        handler.deleteSubcategory(subcategory)
        getCategories(subcategory.category.type)
        getSubcategories(subcategory.category)
    }

    fun addCategory(category: Category) {
        handler.addCategory(category)
        getCategories(category.type)
    }

    fun addSubcategory(subcategory: Subcategory) {
        handler.addSubcategory(subcategory)
        getCategories(subcategory.category.type)
        getSubcategories(subcategory.category)
    }

    fun updateCategory(category: Category, name: String? = null, icon: String? = null) {
        handler.updateCategory(category, name, icon)
        getCategories(category.type)
    }

    fun updateSubcategory(subcategory: Subcategory, name: String) {
        handler.updateSubcategory(subcategory, name)
        getSubcategories(subcategory.category)
    }

}
package viewModel

import core.entity.Category
import core.entity.Subcategory
import core.enums.CategoryType
import domain.category.CategoryHandler
import kotlinx.coroutines.flow.MutableStateFlow

class CategoryViewModel(private val categoryHandler: CategoryHandler = CategoryHandler()) {


    val categories = MutableStateFlow(emptyList<Category>())
    fun getCategories(type: CategoryType) {
        categories.value = categoryHandler.fetchCategories(type)
    }

    val subcategories = MutableStateFlow(emptyList<Subcategory>())
    fun getSubcategories(category: Category) {
        subcategories.value = categoryHandler.fetchSubcategories(category)
    }

    val selectedCategory = MutableStateFlow(Category())
    val selectedSubcategory = MutableStateFlow<Subcategory?>(null)
    val selectedType = MutableStateFlow(CategoryType.INCOME)

    fun deleteCategory(category: Category) {
        categoryHandler.deleteCategory(category)
        getCategories(category.type)
    }

    fun deleteSubcategory(subcategory: Subcategory) {
        categoryHandler.deleteSubcategory(subcategory)
        getCategories(subcategory.category.type)
        getSubcategories(subcategory.category)
    }

    fun addCategory(category: Category) {
        categoryHandler.addCategory(category)
        getCategories(category.type)
    }

    fun addSubcategory(subcategory: Subcategory) {
        categoryHandler.addSubcategory(subcategory)
        getCategories(subcategory.category.type)
        getSubcategories(subcategory.category)
    }

    fun updateCategory(category: Category, name: String? = null, icon: String? = null) {
        categoryHandler.updateCategory(category, name, icon)
        getCategories(category.type)
    }

    fun updateSubcategory(subcategory: Subcategory, name: String) {
        categoryHandler.updateSubcategory(subcategory, name)
        getSubcategories(subcategory.category)
    }

}
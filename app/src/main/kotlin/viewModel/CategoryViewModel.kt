package viewModel

import core.entity.Category
import core.entity.Subcategory
import core.enums.CategoryType
import kotlinx.coroutines.flow.MutableStateFlow
import service.CategoryService

class CategoryViewModel {

    private val service: CategoryService = CategoryService()

    val categories = MutableStateFlow(emptyList<Category>())
    fun getCategories(type: CategoryType) {
        categories.value = service.loadCategoriesList(type) }

    val subcategories = MutableStateFlow(emptyList<Subcategory>())
    fun getSubcategories(category: Category) {
        subcategories.value = service.loadSubCategoriesList(category) }

    val selectedCategory = MutableStateFlow(Category())
    val selectedSubcategory = MutableStateFlow<Subcategory?>(null)
    val selectedType = MutableStateFlow(CategoryType.INCOME)

    fun deleteCategory(category: Category) {
        service.deleteCategory(category)
        getCategories(category.type)
    }

    fun deleteSubcategory(subcategory: Subcategory) {
        service.deleteSubcategory(subcategory)
        getCategories(subcategory.category.type)
        getSubcategories(subcategory.category)
    }

    fun addCategory(category: Category) {
        service.addCategory(category)
        getCategories(category.type)
    }

    fun addSubcategory(subcategory: Subcategory) {
        service.addSubcategory(subcategory)
        getCategories(subcategory.category.type)
        getSubcategories(subcategory.category)
    }

    fun updateCategory(category: Category, name: String? = null, icon: String? = null) {
        val updatedCategory = Category(id = category.id, name = name ?: category.name, type = category.type, icon = icon ?: category.icon, subcategories = category.subcategories)
        service.updateCategory(updatedCategory)
        getCategories(category.type)
    }

    fun updateSubcategory(subcategory: Subcategory, name: String) {
        val updatedSubcategory = Subcategory(subcategory.id, name, subcategory.category)
        service.updateSubcategory(updatedSubcategory)
        getSubcategories(subcategory.category)
    }

}
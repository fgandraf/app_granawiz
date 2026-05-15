package viewModel

import domain.entity.Category
import domain.entity.Subcategory
import domain.enums.CategoryType
import application.category.CategoryHandler
import infrastructure.di.ApplicationContainer
import kotlinx.coroutines.flow.MutableStateFlow
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent

class CategoryViewModel(private val categoryHandler: CategoryHandler = ApplicationContainer.categoryHandler) {

    val categories = MutableStateFlow(emptyList<Category>())
    fun getCategories(type: CategoryType) {
        runCatching {
            categories.value = categoryHandler.fetchCategories(type)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    val subcategories = MutableStateFlow(emptyList<Subcategory>())
    fun getSubcategories(category: Category) {
        runCatching {
            subcategories.value = categoryHandler.fetchSubcategories(category)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    val selectedCategory = MutableStateFlow(Category())
    val selectedSubcategory = MutableStateFlow<Subcategory?>(null)
    val selectedType = MutableStateFlow(CategoryType.INCOME)

    fun deleteCategory(category: Category) {
        runCatching {
            categoryHandler.deleteCategory(category)
            getCategories(category.type)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun deleteSubcategory(subcategory: Subcategory) {
        runCatching {
            categoryHandler.deleteSubcategory(subcategory)
            getCategories(subcategory.category.type)
            getSubcategories(subcategory.category)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun addCategory(category: Category) {
        runCatching {
            categoryHandler.addCategory(category)
            getCategories(category.type)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun addSubcategory(subcategory: Subcategory) {
        runCatching {
            categoryHandler.addSubcategory(subcategory)
            getCategories(subcategory.category.type)
            getSubcategories(subcategory.category)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun updateCategory(category: Category, name: String? = null, icon: String? = null) {
        runCatching {
            categoryHandler.updateCategory(category, name, icon)
            getCategories(category.type)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun updateSubcategory(subcategory: Subcategory, name: String) {
        runCatching {
            categoryHandler.updateSubcategory(subcategory, name)
            getSubcategories(subcategory.category)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }
}

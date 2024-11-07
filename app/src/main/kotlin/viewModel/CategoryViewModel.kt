package viewModel

import domain.entity.Category
import domain.entity.Subcategory
import domain.enums.CategoryType
import application.category.CategoryHandler
import infrastructure.di.ApplicationContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent

class CategoryViewModel(private val categoryHandler: CategoryHandler = ApplicationContainer.categoryHandler) {

    private val _categories = MutableStateFlow(emptyList<Category>())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()
    fun getCategories(type: CategoryType) {
        runCatching {
            _categories.value = categoryHandler.fetchCategories(type)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    private val _subcategories = MutableStateFlow(emptyList<Subcategory>())
    val subcategories: StateFlow<List<Subcategory>> = _subcategories.asStateFlow()
    fun getSubcategories(category: Category) {
        runCatching {
            _subcategories.value = categoryHandler.fetchSubcategories(category)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    private val _selectedCategory = MutableStateFlow(Category())
    val selectedCategory: StateFlow<Category> = _selectedCategory.asStateFlow()
    fun selectCategory(category: Category) { _selectedCategory.value = category }

    private val _selectedSubcategory = MutableStateFlow<Subcategory?>(null)
    val selectedSubcategory: StateFlow<Subcategory?> = _selectedSubcategory.asStateFlow()
    fun selectSubcategory(subcategory: Subcategory?) { _selectedSubcategory.value = subcategory }

    private val _selectedType = MutableStateFlow(CategoryType.INCOME)
    val selectedType: StateFlow<CategoryType> = _selectedType.asStateFlow()
    fun selectType(type: CategoryType) { _selectedType.value = type }

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

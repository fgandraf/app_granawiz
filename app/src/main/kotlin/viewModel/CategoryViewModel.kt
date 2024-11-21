package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import model.dao.CategoryDao
import model.entity.Category
import model.entity.Subcategory
import model.enums.CategoryType

class CategoryViewModel {

    var categories by mutableStateOf(emptyList<Category>()); private set
    var subCategories by mutableStateOf(emptyList<Subcategory>()); private set

    private val _selectedCategory = MutableStateFlow(Category())
    fun selectCategory(category: Category) {
        _selectedCategory.value = category
    }

    private val _selectedType = MutableStateFlow(CategoryType.INCOME)
    fun selectType(type: CategoryType) {
        _selectedType.value = type
    }

    private val categoryDao = CategoryDao()

    fun loadCategories(type: CategoryType) {
        categories = categoryDao.getAll(type)
        subCategories = emptyList()
    }

    fun loadSubCategories(category: Category) {
        subCategories = categories.find{ x -> x == category}?.subcategories!!
    }

    fun deleteCategory(category: Category) {
        categoryDao.delete(category)
        loadCategories(category.type)
    }

    fun deleteSubcategory(subcategory: Subcategory) {
        val categoryId = subcategory.category.id
        categoryDao.deleteSubcategory(subcategory)

        loadCategories(subcategory.category.type)
        subCategories = categories.find { x -> x.id == categoryId }?.subcategories ?: emptyList()
    }

    fun addCategory(name: String, icon: String) {
        val newCategory = Category(name = name, icon = icon, type = _selectedType.value)
        categoryDao.insert(newCategory)
        loadCategories(newCategory.type)
    }

    fun addSubcategory(name: String) {
        val newSubcategory = Subcategory(name = name, category = _selectedCategory.value)
        categoryDao.insertSubcategory(newSubcategory)
        loadCategories(_selectedType.value)
        subCategories = categories.find { x -> x.id == newSubcategory.category.id }?.subcategories ?: emptyList()
    }

    fun updateCategory(category: Category, name: String, icon: String) {
        val updatedCategory = Category(id = category.id, name = name, type = category.type, icon = icon, subcategories = category.subcategories)
        categoryDao.update(updatedCategory)
        loadCategories(category.type)
    }

    fun updateSubcategory(subcategory: Subcategory, name: String) {
        val categoryId = subcategory.category.id
        val updatedSubcategory = Subcategory(subcategory.id, name, subcategory.category)
        categoryDao.updateSubcategory(updatedSubcategory)
        loadCategories(subcategory.category.type)
        subCategories = categories.find { x -> x.id == categoryId }?.subcategories ?: emptyList()
    }

}
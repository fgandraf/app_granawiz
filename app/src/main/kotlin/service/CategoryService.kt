package service

import core.entity.Category
import core.entity.Subcategory
import core.enums.CategoryType
import infra.dao.CategoryDao
import kotlinx.coroutines.flow.MutableStateFlow

class CategoryService {

    private val dao: CategoryDao = CategoryDao()

    var categories = MutableStateFlow(emptyList<Category>())
    val subCategories = MutableStateFlow(emptyList<Subcategory>())


    fun loadCategories(type: CategoryType) {
        categories.value = dao.getAll(type)
    }

    fun loadSubCategories(category: Category) {
        subCategories.value = dao.getAll(category.type)
            .find{ x -> x.id == category.id}
            ?.subcategories
            ?.toList() ?: emptyList()
    }

    fun deleteCategory(category: Category) {
        dao.delete(category)
        loadCategories(category.type)
    }

    fun deleteSubcategory(subcategory: Subcategory) {
        val categoryId = subcategory.category.id
        dao.deleteSubcategory(subcategory)
        loadCategories(subcategory.category.type)
        subCategories.value = categories.value.find { x -> x.id == categoryId }?.subcategories ?: emptyList()
    }

    fun addCategory(type: CategoryType, name: String, icon: String) {
        val newCategory = Category(name = name, icon = icon, type = type)
        dao.insert(newCategory)
        loadCategories(newCategory.type)
    }

    fun addSubcategory(name: String, category: Category) {
        val newSubcategory = Subcategory(name = name, category = category)
        dao.insertSubcategory(newSubcategory)
        loadCategories(category.type)
        subCategories.value = categories.value.find { x -> x.id == newSubcategory.category.id }?.subcategories ?: emptyList()
    }

    fun updateCategory(category: Category, name: String, icon: String) {
        val updatedCategory = Category(id = category.id, name = name, type = category.type, icon = icon, subcategories = category.subcategories)
        dao.update(updatedCategory)
        loadCategories(category.type)
    }

    fun updateSubcategory(subcategory: Subcategory, name: String) {
        val categoryId = subcategory.category.id
        val updatedSubcategory = Subcategory(subcategory.id, name, subcategory.category)
        dao.updateSubcategory(updatedSubcategory)
        loadCategories(subcategory.category.type)
        subCategories.value = categories.value.find { x -> x.id == categoryId }?.subcategories ?: emptyList()
    }
}
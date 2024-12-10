package service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.Category
import core.entity.Subcategory
import core.enums.CategoryType
import infra.dao.CategoryDao

class CategoryService(val dao: CategoryDao = CategoryDao()) {

    var categories by mutableStateOf(emptyList<Category>())
    var subCategories by mutableStateOf(emptyList<Subcategory>())

    fun loadCategories(type: CategoryType) {
        subCategories = emptyList()
        categories = dao.getAll(type)
    }

    fun loadSubCategories(category: Category) {
        subCategories = dao.getAll(category.type).find{ x -> x.id == category.id}?.subcategories?.toList() ?: emptyList()
    }

    fun deleteCategory(category: Category) {
        dao.delete(category)
        loadCategories(category.type)
    }

    fun deleteSubcategory(subcategory: Subcategory) {
        val categoryId = subcategory.category.id
        dao.deleteSubcategory(subcategory)
        loadCategories(subcategory.category.type)
        subCategories = categories.find { x -> x.id == categoryId }?.subcategories ?: emptyList()
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
        subCategories = categories.find { x -> x.id == newSubcategory.category.id }?.subcategories ?: emptyList()
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
        subCategories = categories.find { x -> x.id == categoryId }?.subcategories ?: emptyList()
    }
}
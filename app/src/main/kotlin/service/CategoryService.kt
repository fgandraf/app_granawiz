package service

import core.entity.Category
import core.entity.Subcategory
import core.enums.CategoryType
import infra.dao.CategoryDao

class CategoryService {

    private val dao: CategoryDao = CategoryDao()

    fun loadCategoriesList(type: CategoryType): List<Category> {
        return dao.getAll(type)
    }

    fun loadSubCategoriesList(category: Category): List<Subcategory> {
        return dao.getAll(category.type)
            .find{ x -> x.id == category.id}
            ?.subcategories
            ?.toList() ?: emptyList()
    }

    fun deleteCategory(category: Category) { dao.delete(category) }

    fun deleteSubcategory(subcategory: Subcategory) { dao.deleteSubcategory(subcategory) }

    fun addCategory(category: Category) { dao.insert(category) }

    fun addSubcategory(subcategory: Subcategory) { dao.insertSubcategory(subcategory) }

    fun updateCategory(category: Category) { dao.update(category) }

    fun updateSubcategory(subcategory: Subcategory) { dao.updateSubcategory(subcategory) }
}
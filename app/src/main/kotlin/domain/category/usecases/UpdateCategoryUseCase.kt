package domain.category.usecases

import core.entity.Category
import infra.dao.CategoryDao

class UpdateCategoryUseCase(private val categoryDao: CategoryDao = CategoryDao()) {

    fun execute(category: Category, name: String? = null, icon: String? = null) {
        val updatedCategory = Category(id = category.id, name = name ?: category.name, type = category.type, icon = icon ?: category.icon, subcategories = category.subcategories)
        categoryDao.update(updatedCategory)
    }
}
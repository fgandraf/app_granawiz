package domain.category.usecases

import core.entity.Category
import core.enums.CategoryType
import infra.dao.CategoryDao

class FetchCategoriesUseCases(private val categoryDao: CategoryDao = CategoryDao()) {

    fun execute(type: CategoryType): List<Category> {
        return categoryDao.getAll(type)
    }
}
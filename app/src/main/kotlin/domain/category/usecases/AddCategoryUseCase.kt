package domain.category.usecases

import core.entity.Category
import infra.dao.CategoryDao

class AddCategoryUseCase(private val categoryDao: CategoryDao = CategoryDao()) {


    fun execute(category: Category) {
        categoryDao.insert(category)
    }

}
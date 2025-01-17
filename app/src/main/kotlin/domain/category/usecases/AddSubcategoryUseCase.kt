package domain.category.usecases

import core.entity.Subcategory
import infra.dao.CategoryDao

class AddSubcategoryUseCase(private val categoryDao: CategoryDao = CategoryDao()) {

    fun execute(subcategory: Subcategory) {
        categoryDao.insertSubcategory(subcategory)
    }


}
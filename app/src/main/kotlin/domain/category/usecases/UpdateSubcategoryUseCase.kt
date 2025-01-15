package domain.category.usecases

import core.entity.Subcategory
import infra.dao.CategoryDao

class UpdateSubcategoryUseCase(private val categoryDao: CategoryDao = CategoryDao()) {

    fun execute(subcategory: Subcategory, name: String) {
        val updatedSubcategory = Subcategory(subcategory.id, name, subcategory.category)
        categoryDao.updateSubcategory(updatedSubcategory)
    }
}
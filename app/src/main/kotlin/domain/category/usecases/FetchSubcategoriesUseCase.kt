package domain.category.usecases

import core.entity.Category
import core.entity.Subcategory
import infra.dao.CategoryDao

class FetchSubcategoriesUseCase(private val categoryDao: CategoryDao = CategoryDao()) {


    fun execute(category: Category): List<Subcategory> {
        return categoryDao.getAll(category.type)
            .find { x -> x.id == category.id }
            ?.subcategories
            ?.toList() ?: emptyList()
    }


}
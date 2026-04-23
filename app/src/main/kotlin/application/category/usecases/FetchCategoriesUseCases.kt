package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.entity.Category
import domain.enums.CategoryType
import infrastructure.repository.CategoryRepository

class FetchCategoriesUseCases(private val categoryRepository: ICategoryRepository = CategoryRepository()) {

    fun execute(type: CategoryType): List<Category> {
        return categoryRepository.getAll(type)
    }
}
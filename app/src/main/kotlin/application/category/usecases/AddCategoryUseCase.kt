package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.entity.Category
import infrastructure.repository.CategoryRepository

class AddCategoryUseCase(private val categoryRepository: ICategoryRepository = CategoryRepository()) {


    fun execute(category: Category) {
        categoryRepository.insert(category)
    }

}
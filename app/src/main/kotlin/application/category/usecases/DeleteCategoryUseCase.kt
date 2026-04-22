package application.category.usecases

import domain.entity.Category
import infrastructure.repository.CategoryRepository

class DeleteCategoryUseCase(private val categoryRepository: CategoryRepository = CategoryRepository()) {

    fun execute(category: Category) {
        categoryRepository.delete(category)
    }

}
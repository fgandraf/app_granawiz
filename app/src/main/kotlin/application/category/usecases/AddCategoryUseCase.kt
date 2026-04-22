package application.category.usecases

import domain.entity.Category
import infrastructure.repository.CategoryRepository

class AddCategoryUseCase(private val categoryRepository: CategoryRepository = CategoryRepository()) {


    fun execute(category: Category) {
        categoryRepository.insert(category)
    }

}
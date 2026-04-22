package application.category.usecases

import domain.entity.Subcategory
import infrastructure.repository.CategoryRepository

class AddSubcategoryUseCase(private val categoryRepository: CategoryRepository = CategoryRepository()) {

    fun execute(subcategory: Subcategory) {
        categoryRepository.insertSubcategory(subcategory)
    }


}
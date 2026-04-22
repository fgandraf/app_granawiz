package application.category.usecases

import domain.entity.Subcategory
import infrastructure.repository.CategoryRepository

class DeleteSubcategoryUseCase(private val categoryRepository: CategoryRepository = CategoryRepository()) {

    fun execute(subcategory: Subcategory) {
        categoryRepository.deleteSubcategory(subcategory)
    }


}
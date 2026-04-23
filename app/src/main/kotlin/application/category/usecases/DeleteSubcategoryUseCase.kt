package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.entity.Subcategory
import infrastructure.repository.CategoryRepository

class DeleteSubcategoryUseCase(private val categoryRepository: ICategoryRepository = CategoryRepository()) {

    fun execute(subcategory: Subcategory) {
        categoryRepository.deleteSubcategory(subcategory)
    }


}
package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.entity.Subcategory
import infrastructure.repository.CategoryRepository

class UpdateSubcategoryUseCase(private val categoryRepository: ICategoryRepository = CategoryRepository()) {

    fun execute(subcategory: Subcategory, name: String) {
        val updatedSubcategory = Subcategory(subcategory.id, name, subcategory.category)
        categoryRepository.updateSubcategory(updatedSubcategory)
    }
}
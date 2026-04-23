package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.entity.Subcategory
import infrastructure.repository.CategoryRepository

class AddSubcategoryUseCase(private val categoryRepository: ICategoryRepository = CategoryRepository()) {

    fun execute(subcategory: Subcategory) {
        categoryRepository.insertSubcategory(subcategory)
    }


}
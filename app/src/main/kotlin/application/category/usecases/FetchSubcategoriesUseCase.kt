package application.category.usecases

import domain.entity.Category
import domain.entity.Subcategory
import infrastructure.repository.CategoryRepository

class FetchSubcategoriesUseCase(private val categoryRepository: CategoryRepository = CategoryRepository()) {


    fun execute(category: Category): List<Subcategory> {
        return categoryRepository.getAll(category.type)
            .find { x -> x.id == category.id }
            ?.subcategories
            ?.toList() ?: emptyList()
    }


}
package application.category

import domain.contracts.ICategoryRepository
import domain.entity.Category
import domain.entity.Subcategory
import domain.enums.CategoryType
import application.category.usecases.DeleteCategoryUseCase
import application.category.usecases.DeleteSubcategoryUseCase
import application.category.usecases.FetchSubcategoriesUseCase
import application.category.usecases.UpdateCategoryUseCase
import application.category.usecases.UpdateSubcategoryUseCase
import infrastructure.repository.CategoryRepository

class CategoryHandler {

    private val categoryRepository: ICategoryRepository = CategoryRepository()
    private val fetchSubcategories = FetchSubcategoriesUseCase()
    private val updateCategory = UpdateCategoryUseCase()
    private val updateSubcategory = UpdateSubcategoryUseCase()
    private val deleteCategory = DeleteCategoryUseCase()
    private val deleteSubcategory = DeleteSubcategoryUseCase()

    fun fetchCategories(type: CategoryType): List<Category> = categoryRepository.getAll(type)
    fun fetchSubcategories(category: Category): List<Subcategory> = fetchSubcategories.execute(category)
    fun updateCategory(category: Category, name: String? = null, icon: String? = null) =
        updateCategory.execute(category, name, icon)
    fun updateSubcategory(subcategory: Subcategory, name: String) = updateSubcategory.execute(subcategory, name)
    fun deleteCategory(category: Category) = deleteCategory.execute(category)
    fun deleteSubcategory(subcategory: Subcategory) = deleteSubcategory.execute(subcategory)
    fun addCategory(category: Category) = categoryRepository.insert(category)
    fun addSubcategory(subcategory: Subcategory) = categoryRepository.insertSubcategory(subcategory)
}

package domain.category

import core.entity.Category
import core.entity.Subcategory
import core.enums.CategoryType
import domain.category.usecases.*

class CategoryHandler(
    private val fetchCategories: FetchCategoriesUseCases = FetchCategoriesUseCases(),
    private  val fetchSubcategories: FetchSubcategoriesUseCase = FetchSubcategoriesUseCase(),
    private  val updateCategory: UpdateCategoryUseCase = UpdateCategoryUseCase(),
    private  val updateSubcategory: UpdateSubcategoryUseCase = UpdateSubcategoryUseCase(),
    private  val deleteCategory: DeleteCategoryUseCase = DeleteCategoryUseCase(),
    private  val deleteSubcategory: DeleteSubcategoryUseCase = DeleteSubcategoryUseCase(),
    private  val addCategory: AddCategoryUseCase = AddCategoryUseCase(),
    private  val addSubcategory: AddSubcategoryUseCase = AddSubcategoryUseCase()
) {

    fun fetchCategories(type: CategoryType): List<Category> = fetchCategories.execute(type)
    fun fetchSubcategories(category: Category): List<Subcategory> = fetchSubcategories.execute(category)
    fun updateCategory(category: Category, name: String? = null, icon: String? = null) = updateCategory.execute(category, name, icon)
    fun updateSubcategory(subcategory: Subcategory, name: String) = updateSubcategory.execute(subcategory, name)
    fun deleteCategory(category: Category) = deleteCategory.execute(category)
    fun deleteSubcategory(subcategory: Subcategory) = deleteSubcategory.execute(subcategory)
    fun addCategory(category: Category) = addCategory.execute(category)
    fun addSubcategory(subcategory: Subcategory) = addSubcategory.execute(subcategory)

}
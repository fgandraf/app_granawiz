package application.importStatement.usecases

import application.category.CategoryHandler
import domain.entity.Category
import domain.entity.Subcategory
import domain.enums.CategoryType
import domain.enums.TransactionType
import domain.structs.ParsedEntry

class ResolveOrCreateCategoryUseCase(
    private val categoryHandler: CategoryHandler = CategoryHandler(),
) {
    data class Resolved(val category: Category, val subcategory: Subcategory?)

    private var expenseCategories: MutableList<Category>? = null
    private var incomeCategories: MutableList<Category>? = null

    private fun getCategories(type: CategoryType): MutableList<Category> = when (type) {
        CategoryType.EXPENSE -> expenseCategories
            ?: categoryHandler.fetchCategories(CategoryType.EXPENSE).toMutableList()
                .also { expenseCategories = it }
        CategoryType.INCOME -> incomeCategories
            ?: categoryHandler.fetchCategories(CategoryType.INCOME).toMutableList()
                .also { incomeCategories = it }
    }

    fun execute(entry: ParsedEntry): Resolved {
        entry.category?.let { return Resolved(it, entry.subcategory) }

        val targetType = if (entry.type == TransactionType.GAIN) CategoryType.INCOME else CategoryType.EXPENSE

        val text = entry.customCategoryText?.trim()
        if (!text.isNullOrBlank()) {
            return resolveOrCreateFromText(text, targetType)
        }

        return Resolved(findOrCreateUncategorized(targetType), null)
    }

    private fun resolveOrCreateFromText(text: String, type: CategoryType): Resolved {
        val parts = text.split("/", limit = 2).map { it.trim() }
        val categoryName = parts[0]
        val subName = parts.getOrNull(1)?.takeIf { it.isNotBlank() }

        val categories = getCategories(type)
        val category = categories.firstOrNull { it.name.equals(categoryName, ignoreCase = true) }
            ?: Category(type = type, name = categoryName, icon = "question-mark.svg").also { cat ->
                categoryHandler.addCategory(cat)
                categories.add(cat)
            }

        val subcategory = subName?.let { name ->
            category.subcategories.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: Subcategory(name = name, category = category).also { sub ->
                    categoryHandler.addSubcategory(sub)
                    category.subcategories.add(sub)
                }
        }

        return Resolved(category, subcategory)
    }

    private fun findOrCreateUncategorized(type: CategoryType): Category {
        val categories = getCategories(type)
        return categories.firstOrNull { it.name.equals("Sem categoria", ignoreCase = true) }
            ?: Category(type = type, name = "Sem categoria", icon = "question-mark.svg").also { cat ->
                categoryHandler.addCategory(cat)
                categories.add(cat)
            }
    }
}

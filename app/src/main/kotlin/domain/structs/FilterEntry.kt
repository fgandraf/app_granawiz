package domain.structs

import domain.entity.Category
import domain.entity.Subcategory
import domain.entity.Tag
import domain.enums.TransactionType

data class FilterEntry(
    val partyName: String,
    val description: String,
    val category: Category,
    val subcategory: Subcategory?,
    val tags: List<Tag>?,
    val accountId: Long,
    val type: TransactionType,
)

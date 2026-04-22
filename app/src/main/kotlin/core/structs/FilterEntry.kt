package core.structs

import core.entity.Category
import core.entity.Subcategory
import core.entity.Tag
import core.enums.TransactionType

data class FilterEntry(
    val partyName: String,
    val description: String,
    val category: Category,
    val subcategory: Subcategory?,
    val tags: List<Tag>?,
    val accountId: Long,
    val type: TransactionType,
)

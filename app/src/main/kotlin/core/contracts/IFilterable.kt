package core.contracts

import core.entity.Category
import core.entity.Party
import core.entity.Subcategory
import core.entity.Tag
import core.entity.account.BankAccount
import core.enums.TransactionType
import core.structs.FilterEntry

interface IFilterable {
    val party: Party
    val account: BankAccount
    val category: Category
    val subcategory: Subcategory?
    val tags: List<Tag>?
    val description: String
    val type: TransactionType

    fun toFilterEntry() = FilterEntry(
        partyName = party.name,
        description = description,
        category = category,
        subcategory = subcategory,
        tags = tags,
        accountId = account.id,
        type = type,
    )
}


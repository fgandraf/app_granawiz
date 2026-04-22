package domain.contracts

import domain.entity.Category
import domain.entity.Party
import domain.entity.Subcategory
import domain.entity.Tag
import domain.entity.account.BankAccount
import domain.enums.TransactionType
import domain.structs.FilterEntry

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


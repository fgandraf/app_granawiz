package domain.structs

import domain.entity.Category
import domain.entity.Party
import domain.entity.Subcategory
import domain.enums.TransactionType
import java.time.LocalDateTime
import java.util.UUID

data class ParsedEntry(
    val rowId: String = UUID.randomUUID().toString(),
    val fitId: String?,
    val date: LocalDateTime,
    val rawCounterpartyName: String,
    val description: String,
    val balance: Double,
    val type: TransactionType,
    val party: Party? = null,
    val needsNewParty: Boolean = true,
    val customPartyName: String? = null,
    val category: Category? = null,
    val subcategory: Subcategory? = null,
)
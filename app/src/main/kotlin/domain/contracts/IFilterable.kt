package domain.contracts

import domain.structs.FilterEntry

interface IFilterable {
    fun toFilterEntry(): FilterEntry
}


package domain.contracts

import domain.entity.Party
import domain.entity.PartyName
import domain.enums.PartyType

interface IPartyRepository {

    fun getAll(type: PartyType) : List<Party>

    fun getPartyNameByName(name: String): PartyName?

    fun getPartyByName(name: String): Party?

    fun hasTransactions(party: Party): Boolean

    fun delete(party: Party)

    fun deleteName(partyName: PartyName)

    fun update(party: Party)

    fun updateName(partyName: PartyName)

    fun insert(party: Party)

    fun insertName(partyName: PartyName)
}
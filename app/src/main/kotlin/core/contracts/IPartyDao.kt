package core.contracts

import core.entity.Party
import core.entity.PartyName
import core.enums.PartyType

interface IPartyDao {

    fun getAll(type: PartyType) : List<Party>

    fun getPartyNameByName(name: String): PartyName?

    fun getPartyByName(name: String): Party?

    fun delete(party: Party)

    fun deleteName(partyName: PartyName)

    fun update(party: Party)

    fun updateName(partyName: PartyName)

    fun insert(party: Party)

    fun insertName(partyName: PartyName)
}
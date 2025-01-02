package service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.Party
import core.entity.PartyName
import core.enums.PartyType
import infra.dao.PartyDao

class PartyService {

    private val dao: PartyDao = PartyDao()

    var errorMessage: String? by mutableStateOf(null); private set

    fun loadPartiesList(type: PartyType): List<Party> {
        return dao.getAll(type)
    }

    fun loadNamesList(party: Party): List<PartyName> {
        val parties = dao.getAll(party.type)
        return parties.find{ x -> x.id == party.id}?.partiesNames!!
    }

    fun clearError() { errorMessage = null }

    fun deleteParty(party: Party) { dao.delete(party) }

    fun deletePartyName(partyName: PartyName) { dao.deleteName(partyName) }

    fun addParty(party: Party) { dao.insert(party) }

    fun addPartyName(partyName: PartyName): Boolean {
        val existingName = dao.getPartyNameByName(partyName.name)
        if (existingName != null) {
            errorMessage = "O nome \"${partyName.name}\" já está vinculado à \"${existingName.party.name}\"."
            return false
        }
        dao.insertName(partyName)
        return true
    }

    fun updateParty(party: Party) { dao.update(party) }

    fun updatePartyName(partyName: PartyName): Boolean {
        val existingName = dao.getPartyNameByName(partyName.name)
        if (existingName != null) {
            errorMessage = "O nome \"${partyName.name}\" já está vinculado à \"${existingName.party.name}\"."
            return false
        }
        dao.updateName(partyName)
        return true
    }
}
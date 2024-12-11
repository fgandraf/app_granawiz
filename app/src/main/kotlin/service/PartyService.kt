package service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.Party
import core.entity.PartyName
import core.enums.PartyType
import infra.dao.PartyDao

class PartyService(val dao: PartyDao = PartyDao()) {

    var parties by mutableStateOf(emptyList<Party>())
    var partyNames by mutableStateOf(emptyList<PartyName>())

    var errorMessage: String? by mutableStateOf(null); private set

    fun loadParties(type: PartyType) {
        parties = dao.getAll(type)
    }

    fun loadNames(party: Party) {
        partyNames = parties.find{ x -> x == party}?.partiesNames!!
    }

    fun clearError() {
        errorMessage = null
    }

    fun deleteParty(party: Party) {
        val type = party.type
        dao.delete(party)
        loadParties(type)
    }

    fun deletePartyName(partyName: PartyName) {
        val type = partyName.party.type
        val partyId = partyName.party.id
        dao.deleteName(partyName)

        loadParties(type)
        partyNames = parties.find { x -> x.id == partyId }?.partiesNames ?: emptyList()
    }

    fun addParty(name: String, type: PartyType) {
        val newParty = Party(name = name, type = type)
        dao.insert(newParty)
        loadParties(type)
    }

    fun addPartyName(name: String, party: Party): Boolean {
        val existingName = dao.getPartyNameByName(name)
        if (existingName != null) {
            errorMessage = "O nome \"$name\" já está vinculado à \"${existingName.party.name}\"."
            return false
        }

        val newPartyName = PartyName(name = name, party = party)
        dao.insertName(newPartyName)
        loadParties(party.type)
        partyNames = parties.find { x -> x.id == newPartyName.party.id }?.partiesNames ?: emptyList()
        return true
    }

    fun updateParty(party: Party, name: String) {
        val updatedParty = Party(id = party.id, name = name, type = party.type, partiesNames = party.partiesNames)
        dao.update(updatedParty)
        loadParties(party.type)
    }

    fun updatePartyName(partyName: PartyName, name: String): Boolean {
        val existingName = dao.getPartyNameByName(name)
        if (existingName != null) {
            errorMessage = "O nome \"$name\" já está vinculado à \"${existingName.party.name}\"."
            return false
        }

        val partyId = partyName.party.id
        val updatedPartyName = PartyName(partyName.id, name, partyName.party)
        dao.updateName(updatedPartyName)
        loadParties(partyName.party.type)
        partyNames = parties.find { x -> x.id == partyId }?.partiesNames ?: emptyList()
        return true
    }
}
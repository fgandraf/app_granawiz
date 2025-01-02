package viewModel

import androidx.compose.runtime.derivedStateOf
import core.entity.Party
import core.entity.PartyName
import core.enums.PartyType
import kotlinx.coroutines.flow.MutableStateFlow
import service.PartyService

class PartyViewModel(type: PartyType) {

    val service: PartyService = PartyService()

    var errorMessage = derivedStateOf { service.errorMessage }
    fun clearError(){ service.clearError() }

    var selectedParty = MutableStateFlow<Party?>(null)
    var selectedName = MutableStateFlow<PartyName?>(null)
    val selectedType = MutableStateFlow(type)

    var parties = MutableStateFlow(emptyList<Party>())
    fun getParties() { parties.value = service.loadPartiesList(selectedType.value) }

    var partyNames = MutableStateFlow(emptyList<PartyName>())
    fun getNames() {
        partyNames.value = service.loadNamesList(selectedParty.value!!)
    }

    fun deleteParty(party: Party) {
        service.deleteParty(party)
        getParties()
    }

    fun deleteName(partyName: PartyName) {
        service.deletePartyName(partyName)
        getNames()
    }

    fun addParty(name: String) {
        val newParty = Party(name = name, type = selectedType.value)
        service.addParty(newParty)
        getParties()
        selectedParty.value = newParty
    }

    fun addName(name: String): Boolean{
        val newPartyName = PartyName(name = name, party = selectedParty.value!!)
        val success = service.addPartyName(newPartyName)
        if (!success) return false

        getParties()
        getNames()
        selectedName.value = newPartyName
        return true
    }

    fun updateParty(party: Party, name: String) {
        val updatedParty = Party(id = party.id, name = name, type = party.type, partiesNames = party.partiesNames)
        service.updateParty(updatedParty)
        getParties()
        selectedParty.value = updatedParty
    }

    fun updatePartyName(partyName: PartyName, name: String): Boolean {
        val updatedPartyName = PartyName(id = partyName.id, name = name, party = partyName.party)
        val success = service.updatePartyName(updatedPartyName)
        if (!success) return false

        getParties()
        getNames()
        selectedName.value = updatedPartyName
        return true
    }
}
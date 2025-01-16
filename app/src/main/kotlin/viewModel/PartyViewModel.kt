package viewModel

import androidx.compose.runtime.derivedStateOf
import core.entity.Party
import core.entity.PartyName
import core.enums.PartyType
import domain.party.PartyHandler
import kotlinx.coroutines.flow.MutableStateFlow

class PartyViewModel(type: PartyType, private val partyHandler: PartyHandler = PartyHandler()) {

    var errorMessage = derivedStateOf { partyHandler.errorMessage }
    fun clearError(){ partyHandler.clearError() }

    var selectedParty = MutableStateFlow<Party?>(null)

    var selectedName = MutableStateFlow<PartyName?>(null)
    val selectedType = MutableStateFlow(type)

    var parties = MutableStateFlow(emptyList<Party>())
    fun getParties() { parties.value = partyHandler.fetchParties(selectedType.value) }

    var partyNames = MutableStateFlow(emptyList<PartyName>())
    fun getNames() { partyNames.value = partyHandler.fetchNames(selectedParty.value) }

    fun deleteParty(party: Party) {
        partyHandler.deleteParty(party)
        getParties()
    }

    fun deleteName(partyName: PartyName) {
        partyHandler.deleteName(partyName)
        getNames()
    }

    fun addParty(name: String): Boolean {
        val newParty = partyHandler.addParty(name, selectedType.value) ?: return false
        getParties()
        selectedParty.value = newParty
        return true
    }

    fun updateParty(party: Party, name: String): Boolean {
        val updatedParty = partyHandler.updateParty(party, name) ?: return false
        getParties()
        selectedParty.value = updatedParty
        return true
    }

    fun addName(name: String): Boolean{
        val newPartyName = partyHandler.addName(name, selectedParty.value!!) ?: return false
        getParties(); getNames()
        selectedName.value = newPartyName
        return true
    }

    fun updateName(partyName: PartyName, name: String): Boolean {
        val updatedPartyName = partyHandler.updateName(partyName, name) ?: return false
        getParties(); getNames()
        selectedName.value = updatedPartyName
        return true
    }
}
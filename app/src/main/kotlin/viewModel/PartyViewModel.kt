package viewModel

import androidx.compose.runtime.derivedStateOf
import core.entity.Party
import core.entity.PartyName
import core.enums.PartyType
import kotlinx.coroutines.flow.MutableStateFlow
import domain.party.PartyHander

class PartyViewModel(type: PartyType, private val usecases: PartyHander = PartyHander()) {

    var errorMessage = derivedStateOf { usecases.errorMessage }
    fun clearError(){ usecases.clearError() }

    var selectedParty = MutableStateFlow<Party?>(null)
    var selectedName = MutableStateFlow<PartyName?>(null)
    val selectedType = MutableStateFlow(type)

    var parties = MutableStateFlow(emptyList<Party>())
    fun getParties() { parties.value = usecases.fetchParties(selectedType.value) }

    var partyNames = MutableStateFlow(emptyList<PartyName>())
    fun getNames() {
        if (selectedParty.value != null)
        partyNames.value = usecases.fetchNames(selectedParty.value!!)
    }

    fun deleteParty(party: Party) {
        usecases.deleteParty(party)
        getParties()
    }

    fun deleteName(partyName: PartyName) {
        usecases.deleteName(partyName)
        getNames()
    }

    fun addParty(name: String) {
        val newParty = Party(name = name, type = selectedType.value)
        usecases.addParty(newParty)
        getParties()
        selectedParty.value = newParty
    }

    fun addName(name: String): Boolean{
        val newPartyName = PartyName(name = name, party = selectedParty.value!!)
        val success = usecases.addName(newPartyName)
        if (!success) return false

        getParties()
        getNames()
        selectedName.value = newPartyName
        return true
    }

    fun updateParty(party: Party, name: String) {
        val updatedParty = Party(id = party.id, name = name, type = party.type, partiesNames = party.partiesNames)
        usecases.updateParty(updatedParty)
        getParties()
        selectedParty.value = updatedParty
    }

    fun updatePartyName(partyName: PartyName, name: String): Boolean {
        val updatedPartyName = PartyName(id = partyName.id, name = name, party = partyName.party)
        val success = usecases.updateName(updatedPartyName)
        if (!success) return false

        getParties()
        getNames()
        selectedName.value = updatedPartyName
        return true
    }
}
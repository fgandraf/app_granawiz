package viewModel

import androidx.compose.runtime.derivedStateOf
import core.entity.Party
import core.entity.PartyName
import core.enums.PartyType
import domain.party.PartyHandler
import kotlinx.coroutines.flow.MutableStateFlow

class PartyViewModel(type: PartyType, private val usecases: PartyHandler = PartyHandler()) {

    var errorMessage = derivedStateOf { usecases.errorMessage }
    fun clearError(){ usecases.clearError() }

    var selectedParty = MutableStateFlow<Party?>(null)

    var selectedName = MutableStateFlow<PartyName?>(null)
    val selectedType = MutableStateFlow(type)

    var parties = MutableStateFlow(emptyList<Party>())
    fun getParties() { parties.value = usecases.fetchParties(selectedType.value) }

    var partyNames = MutableStateFlow(emptyList<PartyName>())
    fun getNames() { partyNames.value = usecases.fetchNames(selectedParty.value) }

    fun deleteParty(party: Party) {
        usecases.deleteParty(party)
        getParties()
    }

    fun deleteName(partyName: PartyName) {
        usecases.deleteName(partyName)
        getNames()
    }

    fun addParty(name: String): Boolean {
        val newParty = usecases.addParty(name, selectedType.value) ?: return false
        getParties()
        selectedParty.value = newParty
        return true
    }

    fun updateParty(party: Party, name: String): Boolean {
        val updatedParty = usecases.updateParty(party, name) ?: return false
        getParties()
        selectedParty.value = updatedParty
        return true
    }

    fun addName(name: String): Boolean{
        val newPartyName = usecases.addName(name, selectedParty.value!!) ?: return false
        getParties(); getNames()
        selectedName.value = newPartyName
        return true
    }

    fun updateName(partyName: PartyName, name: String): Boolean {
        val updatedPartyName = usecases.updateName(partyName, name) ?: return false
        getParties(); getNames()
        selectedName.value = updatedPartyName
        return true
    }
}
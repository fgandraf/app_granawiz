package viewModel

import androidx.compose.runtime.derivedStateOf
import domain.entity.Party
import domain.entity.PartyName
import domain.enums.PartyType
import application.party.PartyHandler
import infrastructure.di.ApplicationContainer
import kotlinx.coroutines.flow.MutableStateFlow
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent

class PartyViewModel(type: PartyType, private val partyHandler: PartyHandler = ApplicationContainer.partyHandler) {

    var errorMessage = derivedStateOf { partyHandler.errorMessage }
    fun clearError() {
        partyHandler.clearError()
    }

    var selectedParty = MutableStateFlow<Party?>(null)

    var selectedName = MutableStateFlow<PartyName?>(null)
    val selectedType = MutableStateFlow(type)

    var parties = MutableStateFlow(emptyList<Party>())
    fun getParties() {
        runCatching {
            parties.value = partyHandler.fetchParties(selectedType.value)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    var partyNames = MutableStateFlow(emptyList<PartyName>())
    fun getNames() {
        runCatching {
            partyNames.value = partyHandler.fetchNames(selectedParty.value)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun deleteParty(party: Party): Boolean {
        return runCatching {
            val success = partyHandler.deleteParty(party)
            if (success) getParties()
            success
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .getOrDefault(false)
    }

    fun hasTransactions(party: Party): Boolean {
        return runCatching { partyHandler.hasTransactions(party) }
            .onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
            .getOrDefault(false)
    }

    fun reassignAndDelete(from: Party, to: Party) {
        runCatching {
            partyHandler.reassignAndDelete(from, to)
            getParties()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun deleteName(partyName: PartyName) {
        runCatching {
            partyHandler.deleteName(partyName)
            getNames()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun addParty(name: String): Boolean {
        return runCatching {
            val newParty = partyHandler.addParty(name, selectedType.value) ?: return@runCatching false
            getParties()
            selectedParty.value = newParty
            true
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .getOrDefault(false)
    }

    fun updateParty(party: Party, name: String): Boolean {
        return runCatching {
            val updatedParty = partyHandler.updateParty(party, name) ?: return@runCatching false
            getParties()
            selectedParty.value = updatedParty
            true
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .getOrDefault(false)
    }

    fun addName(name: String): Boolean {
        return runCatching {
            val newPartyName = partyHandler.addName(name, selectedParty.value!!) ?: return@runCatching false
            getParties(); getNames()
            selectedName.value = newPartyName
            true
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .getOrDefault(false)
    }

    fun updateName(partyName: PartyName, name: String): Boolean {
        return runCatching {
            val updatedPartyName = partyHandler.updateName(partyName, name) ?: return@runCatching false
            getParties(); getNames()
            selectedName.value = updatedPartyName
            true
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .getOrDefault(false)
    }
}

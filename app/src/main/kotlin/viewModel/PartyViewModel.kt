package viewModel

import androidx.compose.runtime.derivedStateOf
import domain.entity.Party
import domain.entity.PartyName
import domain.enums.PartyType
import application.party.PartyHandler
import infrastructure.di.ApplicationContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent

class PartyViewModel(type: PartyType, private val partyHandler: PartyHandler = ApplicationContainer.partyHandler) {

    var errorMessage = derivedStateOf { partyHandler.errorMessage }
    fun clearError() {
        partyHandler.clearError()
    }

    private val _selectedParty = MutableStateFlow<Party?>(null)
    val selectedParty: StateFlow<Party?> = _selectedParty.asStateFlow()
    fun selectParty(party: Party?) { _selectedParty.value = party }

    private val _selectedName = MutableStateFlow<PartyName?>(null)
    val selectedName: StateFlow<PartyName?> = _selectedName.asStateFlow()
    fun selectName(name: PartyName?) { _selectedName.value = name }

    private val _selectedType = MutableStateFlow(type)
    val selectedType: StateFlow<PartyType> = _selectedType.asStateFlow()

    private val _parties = MutableStateFlow(emptyList<Party>())
    val parties: StateFlow<List<Party>> = _parties.asStateFlow()
    fun getParties() {
        runCatching {
            _parties.value = partyHandler.fetchParties(_selectedType.value)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    private val _partyNames = MutableStateFlow(emptyList<PartyName>())
    val partyNames: StateFlow<List<PartyName>> = _partyNames.asStateFlow()
    fun getNames() {
        runCatching {
            _partyNames.value = partyHandler.fetchNames(_selectedParty.value)
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
            val newParty = partyHandler.addParty(name, _selectedType.value) ?: return@runCatching false
            getParties()
            _selectedParty.value = newParty
            true
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .getOrDefault(false)
    }

    fun updateParty(party: Party, name: String): Boolean {
        return runCatching {
            val updatedParty = partyHandler.updateParty(party, name) ?: return@runCatching false
            getParties()
            _selectedParty.value = updatedParty
            true
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .getOrDefault(false)
    }

    fun addName(name: String): Boolean {
        return runCatching {
            val newPartyName = partyHandler.addName(name, _selectedParty.value!!) ?: return@runCatching false
            getParties(); getNames()
            _selectedName.value = newPartyName
            true
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .getOrDefault(false)
    }

    fun updateName(partyName: PartyName, name: String): Boolean {
        return runCatching {
            val updatedPartyName = partyHandler.updateName(partyName, name) ?: return@runCatching false
            getParties(); getNames()
            _selectedName.value = updatedPartyName
            true
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .getOrDefault(false)
    }
}

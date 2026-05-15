package application.party

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.entity.PartyName
import domain.enums.PartyType
import application.party.usecases.AddNameUseCase
import application.party.usecases.AddPartyUseCase
import application.party.usecases.DeletePartyUseCase
import application.party.usecases.FetchNamesUseCase
import application.party.usecases.ReassignAndDeletePartyUseCase
import application.party.usecases.UpdateNameUseCase
import application.party.usecases.UpdatePartyUseCase

class PartyHandler(
    private val partyRepository: IPartyRepository,
    private val deletePartyUseCase: DeletePartyUseCase,
    private val reassignAndDeletePartyUseCase: ReassignAndDeletePartyUseCase,
    private val addPartyUseCase: AddPartyUseCase,
    private val updatePartyUseCase: UpdatePartyUseCase,
    private val addNameUseCase: AddNameUseCase,
    private val updateNameUseCase: UpdateNameUseCase,
    private val fetchNamesUseCase: FetchNamesUseCase,
) {

    var errorMessage: String? by mutableStateOf(null); private set
    fun clearError() { errorMessage = null }

    fun fetchParties(type: PartyType): List<Party> = partyRepository.getAll(type)
    fun fetchNames(party: Party?): List<PartyName> = fetchNamesUseCase.execute(party)
    fun deleteParty(party: Party): Boolean {
        val error = deletePartyUseCase.execute(party)
        errorMessage = error
        return error == null
    }
    fun deleteName(partyName: PartyName) = partyRepository.deleteName(partyName)
    fun hasTransactions(party: Party): Boolean = partyRepository.hasTransactions(party)
    fun reassignAndDelete(from: Party, to: Party) = reassignAndDeletePartyUseCase.execute(from, to)

    fun addParty(name: String, type: PartyType): Party? {
        val response = addPartyUseCase.execute(name, type)
        errorMessage = response.first
        return response.second
    }

    fun updateParty(party: Party, name: String): Party? {
        val response = updatePartyUseCase.execute(party, name)
        errorMessage = response.first
        return response.second
    }

    fun addName(name: String, party: Party): PartyName? {
        val response = addNameUseCase.execute(name, party)
        errorMessage = response.first
        return response.second
    }

    fun updateName(partyName: PartyName, name: String): PartyName? {
        val response = updateNameUseCase.execute(partyName, name)
        errorMessage = response.first
        return response.second
    }
}

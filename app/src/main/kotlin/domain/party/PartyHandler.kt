package domain.party

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.Party
import core.entity.PartyName
import core.enums.PartyType
import domain.party.usecases.*

class PartyHandler{

    private val fetchPartiesUseCase = FetchPartiesUseCase()
    private val fetchNamesUseCase = FetchNamesUseCase()
    private val deletePartyUseCase = DeletePartyUseCase()
    private val deleteNameUseCase = DeleteNameUseCase()
    private val addPartyUseCase = AddPartyUseCase()
    private val updatePartyUseCase = UpdatePartyUseCase()
    private val addNameUseCase = AddNameUseCase()
    private val updateNameUseCase = UpdateNameUseCase()


    var errorMessage: String? by mutableStateOf(null); private set
    fun clearError() { errorMessage = null }
    
    fun fetchParties(type: PartyType): List<Party> = fetchPartiesUseCase.execute(type)
    fun fetchNames(party: Party?): List<PartyName> = fetchNamesUseCase.execute(party)
    fun deleteParty(party: Party) = deletePartyUseCase.execute(party)
    fun deleteName(partyName: PartyName) = deleteNameUseCase.execute(partyName)

    fun addParty(name: String, type: PartyType): Party?{
        val response = addPartyUseCase.execute(name, type)
        errorMessage = response.first
        return response.second

    }

    fun updateParty(party: Party, name: String): Party? {
        val response = updatePartyUseCase.execute(party, name)
        errorMessage = response.first
        return response.second
    }


    fun addName(name: String, party: Party): PartyName?{
        val response = addNameUseCase.execute(name, party)
        errorMessage = response.first
        return response.second
    }

    fun updateName(partyName: PartyName, name: String): PartyName?{
       val response = updateNameUseCase.execute(partyName, name)
        errorMessage = response.first
        return response.second
    }

}
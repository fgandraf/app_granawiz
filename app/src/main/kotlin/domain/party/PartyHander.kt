package domain.party

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.Party
import core.entity.PartyName
import core.enums.PartyType
import domain.party.usecases.*

class PartyHander{

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
    fun fetchNames(party: Party): List<PartyName> = fetchNamesUseCase.execute(party)
    fun deleteParty(party: Party) = deletePartyUseCase.execute(party)
    fun deleteName(partyName: PartyName) = deleteNameUseCase.execute(partyName)
    fun addParty(party: Party) = addPartyUseCase.execute(party)
    fun updateParty(party: Party) = updatePartyUseCase.execute(party)
    fun addName(partyName: PartyName): Boolean{
        val message = addNameUseCase.execute(partyName)
        if (message == "") return true
        else errorMessage = message; return false
    }
    fun updateName(partyName: PartyName): Boolean{
        val message = updateNameUseCase.execute(partyName)
        if (message == "") return true
        else errorMessage = message; return false
    }

}
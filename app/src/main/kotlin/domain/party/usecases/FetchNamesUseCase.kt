package domain.party.usecases

import core.entity.Party
import core.entity.PartyName
import infra.dao.PartyDao

class FetchNamesUseCase(private val partyDao: PartyDao = PartyDao()) {


    fun execute(party: Party?): List<PartyName> {
        if (party == null)
            return emptyList()

        val parties = partyDao.getAll(party.type)
        return parties.find{ x -> x.id == party.id}?.partiesNames!!
    }


}
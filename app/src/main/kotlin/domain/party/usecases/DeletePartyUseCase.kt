package domain.party.usecases

import core.entity.Party
import infra.dao.PartyDao

class DeletePartyUseCase(private val partyDao: PartyDao = PartyDao()) {


    fun execute(party: Party) {
        partyDao.delete(party)
    }

}
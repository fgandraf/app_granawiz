package domain.party.usecases

import core.entity.Party
import infra.dao.PartyDao

class AddPartyUseCase(private val partyDao: PartyDao = PartyDao()) {


    fun execute(party: Party) {
        partyDao.insert(party)
    }


}
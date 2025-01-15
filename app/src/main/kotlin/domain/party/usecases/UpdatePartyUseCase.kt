package domain.party.usecases

import core.entity.Party
import infra.dao.PartyDao

class UpdatePartyUseCase(private val partyDao: PartyDao = PartyDao()) {


    fun execute(party: Party) {
        partyDao.update(party)
    }


}
package domain.party.usecases

import core.entity.PartyName
import infra.dao.PartyDao

class DeleteNameUseCase(private val partyDao: PartyDao = PartyDao()) {


    fun execute(partyName: PartyName) {
        partyDao.deleteName(partyName)
    }


}
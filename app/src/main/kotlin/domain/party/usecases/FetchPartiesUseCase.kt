package domain.party.usecases

import core.entity.Party
import core.enums.PartyType
import infra.dao.PartyDao

class FetchPartiesUseCase(private val partyDao: PartyDao = PartyDao()) {

    fun execute(type: PartyType): List<Party> {
        return partyDao.getAll(type)
    }

}
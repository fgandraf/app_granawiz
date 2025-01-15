package domain.party.usecases

import core.entity.PartyName
import infra.dao.PartyDao

class UpdateNameUseCase(private val partyDao: PartyDao = PartyDao()) {


    fun execute(partyName: PartyName): String {
        val existingName = partyDao.getPartyNameByName(partyName.name)
        if (existingName != null)
            return "O nome \"${partyName.name}\" já está vinculado à \"${existingName.party.name}\"."

        partyDao.updateName(partyName)
        return ""
    }


}
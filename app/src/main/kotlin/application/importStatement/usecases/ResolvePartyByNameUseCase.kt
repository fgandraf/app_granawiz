package application.importStatement.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.enums.PartyType
class ResolvePartyByNameUseCase(
    private val partyRepository: IPartyRepository,
) {
    fun execute(rawName: String, type: PartyType? = null): Party? {
        val trimmed = rawName.trim()
        if (trimmed.isEmpty()) return null
        partyRepository.getPartyNameByName(trimmed)?.let {
            if (type == null || it.party.type == type) return it.party
        }
        return if (type != null) partyRepository.getPartyByNameAndType(trimmed, type)
        else partyRepository.getPartyByName(trimmed)
    }
}

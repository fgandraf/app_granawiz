package application.importStatement.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
class ResolvePartyByNameUseCase(
    private val partyRepository: IPartyRepository,
) {
    fun execute(rawName: String): Party? {
        val trimmed = rawName.trim()
        if (trimmed.isEmpty()) return null
        partyRepository.getPartyNameByName(trimmed)?.let { return it.party }
        return partyRepository.getPartyByName(trimmed)
    }
}

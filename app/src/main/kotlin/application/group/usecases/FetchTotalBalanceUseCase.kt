package application.group.usecases

import domain.contracts.IGroupRepository
import infrastructure.repository.GroupRepository

class FetchTotalBalanceUseCase(private val groupRepository: IGroupRepository = GroupRepository()) {


    fun execute(): Double {
        return groupRepository.getAll().sumOf { group ->
            group.accounts.sumOf {
                it.balance
            }
        }
    }


}
package application.group.usecases

import infrastructure.repository.GroupRepository

class FetchTotalBalanceUseCase(private val groupRepository: GroupRepository = GroupRepository()) {


    fun execute(): Double {
        return groupRepository.getAll().sumOf { group ->
            group.accounts.sumOf {
                it.balance
            }
        }
    }


}
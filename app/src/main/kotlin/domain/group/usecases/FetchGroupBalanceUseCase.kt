package domain.group.usecases

import core.entity.Group

class FetchGroupBalanceUseCase {


    fun execute(group: Group) : Double {
        return group.accounts.sumOf { it.balance }
    }

}
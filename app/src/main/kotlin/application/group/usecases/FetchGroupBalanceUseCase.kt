package application.group.usecases

import domain.entity.Group

class FetchGroupBalanceUseCase {


    fun execute(group: Group): Double {
        return group.accounts.sumOf { it.balance }
    }

}
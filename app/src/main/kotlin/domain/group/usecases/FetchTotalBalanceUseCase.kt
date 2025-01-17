package domain.group.usecases

import infra.dao.GroupDao

class FetchTotalBalanceUseCase(private val groupDao: GroupDao = GroupDao()) {


    fun execute(): Double {
        return groupDao.getAll().sumOf { group ->
            group.accounts.sumOf {
                it.balance
            }
        }
    }


}
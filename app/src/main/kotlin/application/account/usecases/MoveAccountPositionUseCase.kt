package application.account.usecases

import domain.contracts.IAccountRepository
import domain.entity.Group
import domain.entity.account.BankAccount
import infrastructure.repository.AccountRepository

class MoveAccountPositionUseCase(private val accountRepository: IAccountRepository = AccountRepository()) {


    fun execute(groups: List<Group>, account: BankAccount, direction: Int) {
        val group = account.group
        val groupIndex = groups.indexOf(group)
        if (groupIndex != -1) {
            val accountIndex = groups[groupIndex].accounts.indexOf(account)
            val newIndex = accountIndex + direction
            if (newIndex in 0 until groups[groupIndex].accounts.size) {
                val accounts = groups[groupIndex].accounts.toMutableList()
                accounts[accountIndex] = accounts[newIndex]
                accounts[newIndex] = account
                accounts.forEachIndexed { index, acc -> acc.position = index + 1 }
                accountRepository.updateAccountPositions(accounts)
            }
        }
    }


}
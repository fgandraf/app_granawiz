package domain.account

import core.entity.Group
import core.entity.account.BankAccount
import core.enums.AccountType
import domain.account.usecases.DeleteAccountUseCase
import domain.account.usecases.MoveAccountPositionUseCase
import domain.account.usecases.SaveAccountUseCase
import viewModel.AccountFormViewModel

class AccountHandler{

    private val deleteAccount = DeleteAccountUseCase()
    private val moveAccountPosition = MoveAccountPositionUseCase()
    private val saveAccount = SaveAccountUseCase()


    fun deleteAccount(account: BankAccount) = deleteAccount.execute(account)
    fun moveAccountPosition(groups: List<Group>, account: BankAccount, direction: Int) = moveAccountPosition.execute(groups, account, direction)
    fun saveAccount(viewModel: AccountFormViewModel? = null, type: AccountType, account: BankAccount? = null) = saveAccount.execute(viewModel, type, account)

}
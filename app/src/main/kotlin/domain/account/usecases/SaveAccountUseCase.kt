package domain.account.usecases

import core.entity.account.BankAccount
import core.entity.account.CheckingAccount
import core.entity.account.CreditCardAccount
import core.entity.account.SavingsAccount
import core.enums.AccountType
import infra.dao.AccountDao
import viewModel.AccountFormViewModel

class SaveAccountUseCase(private val dao: AccountDao = AccountDao()) {



    fun execute(viewModel: AccountFormViewModel? = null, type: AccountType, account: BankAccount? = null) {

        val newOrUpdatedAccount = when (type) {
            AccountType.CHECKING ->
                if (account == null) buildAccount(viewModel!!, AccountType.CHECKING)
                else buildAccount(viewModel!!, AccountType.CHECKING, id = account.id, position = account.position)
            AccountType.SAVINGS ->
                if (account == null) buildAccount(viewModel!!, AccountType.SAVINGS)
                else buildAccount(viewModel!!, AccountType.SAVINGS, id = account.id, position = account.position)
            AccountType.CREDIT_CARD ->
                if (account == null) buildAccount(viewModel!!, AccountType.CREDIT_CARD)
                else buildAccount(viewModel!!, AccountType.CREDIT_CARD, id = account.id, position = account.position)
        }

        if (account == null) dao.insert(newOrUpdatedAccount)
        else dao.update(newOrUpdatedAccount)
    }




    private fun buildAccount(viewModel: AccountFormViewModel, accountType: AccountType, id: Long? = null, position: Int? = null) : BankAccount {
        when(accountType){
            AccountType.CHECKING -> {
                return CheckingAccount(
                    id = id,
                    name = viewModel.name,
                    description = viewModel.description,
                    position = position ?: (viewModel.group.accounts.size + 1),
                    icon = viewModel.icon,
                    balance = viewModel.balance,
                    group = viewModel.group,
                    openBalance = viewModel.balance,
                    overdraftLimit = viewModel.limit
                )
            }
            AccountType.SAVINGS -> {
                return SavingsAccount(
                    id = id ?: 0L,
                    name = viewModel.name,
                    description = viewModel.description,
                    position = position ?: (viewModel.group.accounts.size + 1),
                    icon = viewModel.icon,
                    balance = viewModel.balance,
                    group = viewModel.group,
                    openBalance = viewModel.balance
                )
            }
            AccountType.CREDIT_CARD -> {
                return CreditCardAccount(
                    id = id ?: 0L,
                    name = viewModel.name,
                    description = viewModel.description,
                    position = position ?: (viewModel.group.accounts.size + 1),
                    icon = viewModel.icon,
                    balance = 0.0,
                    group = viewModel.group,
                    creditLimit = viewModel.limit,
                    closingDay = viewModel.closingDay,
                    dueDay = viewModel.dueDay
                )
            }
        }
    }

}
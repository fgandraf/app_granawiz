package domain.account.usecases

import core.entity.account.BankAccount
import core.entity.account.CheckingAccount
import core.entity.account.CreditCardAccount
import core.entity.account.SavingsAccount
import core.enums.AccountType
import viewModel.AccountFormViewModel

class BuildAccountFromModelUseCase {

    fun execute(viewModel: AccountFormViewModel) : BankAccount {
        when(viewModel.type){
            AccountType.CHECKING -> {
                return CheckingAccount(
                    id = viewModel.id,
                    name = viewModel.name,
                    description = viewModel.description,
                    position = viewModel.position,
                    icon = viewModel.icon,
                    balance = viewModel.balance,
                    group = viewModel.group,
                    openBalance = viewModel.balance,
                    overdraftLimit = viewModel.limit
                )
            }
            AccountType.SAVINGS -> {
                return SavingsAccount(
                    id = viewModel.id,
                    name = viewModel.name,
                    description = viewModel.description,
                    position = viewModel.position,
                    icon = viewModel.icon,
                    balance = viewModel.balance,
                    group = viewModel.group,
                    openBalance = viewModel.balance
                )
            }
            AccountType.CREDIT_CARD -> {
                return CreditCardAccount(
                    id = viewModel.id,
                    name = viewModel.name,
                    description = viewModel.description,
                    position = viewModel.position,
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
package view.modules

import domain.entity.account.BankAccount

sealed class Screen {
    data object Dashboard : Screen()
    data object Schedules : Screen()
    data object Categories : Screen()
    data object Tags : Screen()
    data object Receivers : Screen()
    data object Payers : Screen()
    data class Transactions(val account: BankAccount? = null, val showAddButton: Boolean = false) : Screen()
    data class NewTransactionForm(val transactions: Transactions) : Screen()
    data class ImportStatement(val account: BankAccount? = null) : Screen()
}
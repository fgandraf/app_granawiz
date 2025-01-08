package view.modules

import core.entity.account.BankAccount

sealed class Screen {
    data object Dashboard : Screen()
    data object Schedules : Screen()
    data object Reports : Screen()
    data object Database : Screen()
    data object Categories : Screen()
    data object Tags : Screen()
    data object Receivers : Screen()
    data object Payers : Screen()
    data class Transactions(val account: BankAccount? = null, val showAddButton: Boolean = false) : Screen()
}
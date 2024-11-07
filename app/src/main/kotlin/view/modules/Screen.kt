package view.modules

import model.entity.account.BankAccount

sealed class Screen {
    data object Dashboard : Screen()
    data object Schedules : Screen()
    data object Reports : Screen()
    data object Categories : Screen()
    data object Tags : Screen()
    data object Receivers : Screen()
    data object Payers : Screen()
    data class Transactions(val account: BankAccount?) : Screen()
}
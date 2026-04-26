package view.modules

import androidx.compose.runtime.Composable
import view.modules.categories.CategoriesScreen
import view.modules.dashboard.DashboardScreen
import view.modules.party.PayersScreen
import view.modules.party.ReceiversScreen
import view.modules.schedules.ScheduleScreen
import view.modules.tags.TagsScreen
import view.modules.importStatement.ImportStatementScreen
import view.modules.transactions.TransactionsScreen

@Composable
fun MainContent(screen: Screen, onScreenChange: (Screen) -> Unit, onSidebarReload: () -> Unit = {}) {
    when (screen) {
        is Screen.Dashboard -> DashboardScreen()
        is Screen.Schedules -> ScheduleScreen()
        is Screen.Categories -> CategoriesScreen()
        is Screen.Tags -> TagsScreen()
        is Screen.Receivers -> ReceiversScreen()
        is Screen.Payers -> PayersScreen()
        is Screen.Transactions -> TransactionsScreen(screen.account, screen.showAddButton, onScreenChange = onScreenChange, onSidebarReload = onSidebarReload)
        is Screen.NewTransactionForm -> TransactionsScreen(screen.transactions.account, screen.transactions.showAddButton, onScreenChange = onScreenChange, onSidebarReload = onSidebarReload)
        is Screen.ImportStatement -> ImportStatementScreen(account = screen.account, onScreenChange = onScreenChange, onSidebarReload = onSidebarReload)
    }
}
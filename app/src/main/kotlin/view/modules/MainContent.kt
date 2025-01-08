package view.modules

import androidx.compose.runtime.Composable
import view.modules.categories.CategoriesScreen
import view.modules.dashboard.DashboardScreen
import view.modules.database.DatabaseScreen
import view.modules.party.PayersScreen
import view.modules.party.ReceiversScreen
import view.modules.reports.ReportsScreen
import view.modules.schedules.ScheduleScreen
import view.modules.tags.TagsScreen
import view.modules.transactions.TransactionsScreen

@Composable
fun MainContent(screen: Screen) {
    when (screen) {
        is Screen.Dashboard -> DashboardScreen()
        is Screen.Schedules -> ScheduleScreen()
        is Screen.Reports -> ReportsScreen()
        is Screen.Database -> DatabaseScreen()
        is Screen.Categories -> CategoriesScreen()
        is Screen.Tags -> TagsScreen()
        is Screen.Receivers -> ReceiversScreen()
        is Screen.Payers -> PayersScreen()
        is Screen.Transactions -> TransactionsScreen(screen.account, screen.showAddButton)
    }
}
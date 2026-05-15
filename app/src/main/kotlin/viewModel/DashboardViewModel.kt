package viewModel

import domain.structs.DashboardPeriod
import domain.structs.DashboardSummary
import application.dashboard.DashboardHandler
import infrastructure.di.ApplicationContainer
import kotlinx.coroutines.flow.MutableStateFlow

class DashboardViewModel(
    private val dashboardHandler: DashboardHandler = ApplicationContainer.dashboardHandler,
) {

    val summary = MutableStateFlow<DashboardSummary?>(null)
    val period = MutableStateFlow<DashboardPeriod>(DashboardPeriod.ThisMonth)
    val isLoading = MutableStateFlow(false)

    fun selectPeriod(newPeriod: DashboardPeriod) {
        period.value = newPeriod
        reload()
    }

    fun reload() {
        isLoading.value = true
        summary.value = dashboardHandler.buildSummary(period.value)
        isLoading.value = false
    }

    init {
        reload()
    }
}

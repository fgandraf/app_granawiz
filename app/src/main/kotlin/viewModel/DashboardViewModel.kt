package viewModel

import core.structs.DashboardPeriod
import core.structs.DashboardSummary
import domain.dashboard.DashboardHandler
import kotlinx.coroutines.flow.MutableStateFlow

class DashboardViewModel(
    private val dashboardHandler: DashboardHandler = DashboardHandler(),
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

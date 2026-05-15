package viewModel

import domain.structs.DashboardPeriod
import domain.structs.DashboardSummary
import application.dashboard.DashboardHandler
import infrastructure.di.ApplicationContainer
import kotlinx.coroutines.flow.MutableStateFlow
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent

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
        runCatching {
            isLoading.value = true
            summary.value = dashboardHandler.buildSummary(period.value)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .also { isLoading.value = false }
    }

    init {
        reload()
    }
}

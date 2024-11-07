package viewModel

import domain.structs.DashboardPeriod
import domain.structs.DashboardSummary
import application.dashboard.DashboardHandler
import infrastructure.di.ApplicationContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent

class DashboardViewModel(
    private val dashboardHandler: DashboardHandler = ApplicationContainer.dashboardHandler,
) {

    private val _summary = MutableStateFlow<DashboardSummary?>(null)
    val summary: StateFlow<DashboardSummary?> = _summary.asStateFlow()

    private val _period = MutableStateFlow<DashboardPeriod>(DashboardPeriod.ThisMonth)
    val period: StateFlow<DashboardPeriod> = _period.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun selectPeriod(newPeriod: DashboardPeriod) {
        _period.value = newPeriod
        reload()
    }

    fun reload() {
        runCatching {
            _isLoading.value = true
            _summary.value = dashboardHandler.buildSummary(_period.value)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .also { _isLoading.value = false }
    }

    init {
        reload()
    }
}

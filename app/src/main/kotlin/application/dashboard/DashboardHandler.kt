package application.dashboard

import domain.structs.DashboardPeriod
import domain.structs.DashboardSummary
import application.dashboard.usecases.BuildDashboardSummaryUseCase

class DashboardHandler(
    private val buildDashboardSummaryUseCase: BuildDashboardSummaryUseCase,
) {

    fun buildSummary(period: DashboardPeriod): DashboardSummary =
        buildDashboardSummaryUseCase.execute(period)
}

package domain.dashboard

import core.structs.DashboardPeriod
import core.structs.DashboardSummary
import domain.dashboard.usecases.BuildDashboardSummaryUseCase

class DashboardHandler(
    private val buildDashboardSummaryUseCase: BuildDashboardSummaryUseCase = BuildDashboardSummaryUseCase(),
) {

    fun buildSummary(period: DashboardPeriod): DashboardSummary =
        buildDashboardSummaryUseCase.execute(period)
}

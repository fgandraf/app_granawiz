package application.dashboard.usecases

import application.schedule.usecases.FetchSchedulesUseCase
import application.schedule.usecases.GenerateOccurrencesUseCase
import application.schedule.usecases.ScheduleOccurrence
import domain.contracts.ITransactionRepository
import domain.entity.Schedule
import domain.enums.ScheduleFrequency
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class FetchUpcomingOccurrencesUseCaseTest {

    private val fetchSchedules = mockk<FetchSchedulesUseCase>()
    private val generateOccurrences = mockk<GenerateOccurrencesUseCase>()
    private val txRepo = mockk<ITransactionRepository>()
    private val useCase = FetchUpcomingOccurrencesUseCase(fetchSchedules, generateOccurrences, txRepo)

    private val today = LocalDate.of(2024, 3, 15)

    private fun occurrence(dueDate: LocalDateTime) = ScheduleOccurrence(
        schedule = Schedule().copy(frequency = ScheduleFrequency.MONTHLY, startDate = dueDate),
        dueDate = dueDate,
        index = 0,
    )

    @Test
    fun `returns occurrences sorted by due date`() {
        val sched = Schedule()
        every { fetchSchedules.execute() } returns listOf(sched)
        every { txRepo.getAll() } returns emptyList()
        val occ1 = occurrence(LocalDateTime.of(2024, 5, 1, 0, 0))
        val occ2 = occurrence(LocalDateTime.of(2024, 3, 20, 0, 0))
        every { generateOccurrences.execute(sched, any(), any(), any()) } returns listOf(occ1, occ2)

        val result = useCase.execute(today)

        assertEquals(occ2.dueDate, result[0].dueDate)
        assertEquals(occ1.dueDate, result[1].dueDate)
    }

    @Test
    fun `respects limit parameter`() {
        val sched = Schedule()
        every { fetchSchedules.execute() } returns listOf(sched)
        every { txRepo.getAll() } returns emptyList()
        val occs = (1..10).map { occurrence(LocalDateTime.of(2024, 3, it, 0, 0)) }
        every { generateOccurrences.execute(sched, any(), any(), any()) } returns occs

        val result = useCase.execute(today, limit = 3)

        assertEquals(3, result.size)
    }

    @Test
    fun `returns empty when no schedules exist`() {
        every { fetchSchedules.execute() } returns emptyList()
        every { txRepo.getAll() } returns emptyList()

        val result = useCase.execute(today)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `passes paid transactions to generate occurrences`() {
        val sched = Schedule()
        val paidTx = mockk<domain.entity.Transaction>()
        every { fetchSchedules.execute() } returns listOf(sched)
        every { txRepo.getAll() } returns listOf(paidTx)
        every { generateOccurrences.execute(sched, any(), any(), listOf(paidTx)) } returns emptyList()

        useCase.execute(today)

        verify { generateOccurrences.execute(sched, any(), any(), listOf(paidTx)) }
    }
}

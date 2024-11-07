package application.schedule.usecases

import domain.contracts.IScheduleRepository
import domain.entity.Schedule
import domain.enums.ScheduleFrequency
import io.mockk.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DeleteScheduleUseCaseTest {

    private val repo = mockk<IScheduleRepository>(relaxed = true)
    private val useCase = DeleteScheduleUseCase(repo)

    private val jan1 = LocalDateTime.of(2024, 1, 1, 0, 0)
    private val feb1 = LocalDateTime.of(2024, 2, 1, 0, 0)
    private val mar1 = LocalDateTime.of(2024, 3, 1, 0, 0)

    @Test
    fun `execute deletes the schedule directly`() {
        val sched = Schedule()
        useCase.execute(sched)
        verify { repo.delete(sched) }
    }

    // ── deleteThisAndFuture ───────────────────────────────────────────────────

    @Test
    fun `deleteThisAndFuture at index 0 deletes the schedule`() {
        val sched = Schedule().copy(frequency = ScheduleFrequency.MONTHLY, startDate = jan1)
        val occ = ScheduleOccurrence(sched, jan1, index = 0)
        useCase.deleteThisAndFuture(occ)
        verify { repo.delete(sched) }
    }

    @Test
    fun `deleteThisAndFuture at index greater than 0 updates end date`() {
        val sched = Schedule().copy(frequency = ScheduleFrequency.MONTHLY, startDate = jan1)
        val occ = ScheduleOccurrence(sched, feb1, index = 1)
        useCase.deleteThisAndFuture(occ)
        verify { repo.update(any()) }
        verify(exactly = 0) { repo.delete(any()) }
    }

    // ── deleteThisOccurrence – ONCE ───────────────────────────────────────────

    @Test
    fun `deleteThisOccurrence for ONCE frequency deletes schedule`() {
        val sched = Schedule().copy(frequency = ScheduleFrequency.ONCE, startDate = jan1)
        val occ = ScheduleOccurrence(sched, jan1, index = 0)
        useCase.deleteThisOccurrence(occ)
        verify { repo.delete(sched) }
    }

    // ── deleteThisOccurrence – index 0 ───────────────────────────────────────

    @Test
    fun `deleteThisOccurrence at index 0 updates schedule start to next occurrence`() {
        val sched = Schedule().copy(frequency = ScheduleFrequency.DAILY, startDate = jan1, interval = 1)
        val occ = ScheduleOccurrence(sched, jan1, index = 0)
        useCase.deleteThisOccurrence(occ)
        verify { repo.update(any()) }
        verify(exactly = 0) { repo.delete(any()) }
    }

    @Test
    fun `deleteThisOccurrence at index 0 with single installment deletes schedule`() {
        val sched = Schedule().copy(frequency = ScheduleFrequency.MONTHLY, startDate = jan1, installments = 1)
        val occ = ScheduleOccurrence(sched, jan1, index = 0)
        useCase.deleteThisOccurrence(occ)
        verify { repo.delete(sched) }
    }

    // ── deleteThisOccurrence – index > 0 ─────────────────────────────────────

    @Test
    fun `deleteThisOccurrence at mid index splits into two schedules`() {
        val sched = Schedule().copy(
            frequency = ScheduleFrequency.MONTHLY,
            startDate = jan1,
            installments = 5,
        )
        val occ = ScheduleOccurrence(sched, feb1, index = 1)
        useCase.deleteThisOccurrence(occ)
        verify { repo.update(any()) }
        verify { repo.insert(any()) }
    }

    @Test
    fun `deleteThisOccurrence at last installment index only updates end date`() {
        val sched = Schedule().copy(
            frequency = ScheduleFrequency.MONTHLY,
            startDate = jan1,
            installments = 2,
        )
        val occ = ScheduleOccurrence(sched, feb1, index = 1)
        useCase.deleteThisOccurrence(occ)
        verify { repo.update(any()) }
        verify(exactly = 0) { repo.insert(any()) }
    }
}

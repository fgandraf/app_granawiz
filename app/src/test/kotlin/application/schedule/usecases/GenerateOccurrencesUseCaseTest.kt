package application.schedule.usecases

import domain.entity.Schedule
import domain.entity.Transaction
import domain.enums.ScheduleFrequency
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class GenerateOccurrencesUseCaseTest {

    private val useCase = GenerateOccurrencesUseCase()

    private val jan1 = LocalDateTime.of(2024, 1, 1, 0, 0)
    private val dec31 = LocalDateTime.of(2024, 12, 31, 23, 59)

    private fun schedule(
        frequency: ScheduleFrequency = ScheduleFrequency.ONCE,
        startDate: LocalDateTime = jan1,
        interval: Int = 1,
        dayOfMonth: Int? = null,
        endDate: LocalDateTime? = null,
        installments: Int? = null,
    ) = Schedule().copy(
        frequency = frequency,
        startDate = startDate,
        interval = interval,
        dayOfMonth = dayOfMonth,
        endDate = endDate,
        installments = installments,
    )

    // ── ONCE ─────────────────────────────────────────────────────────────────

    @Test
    fun `ONCE within window returns exactly one occurrence`() {
        val result = useCase.execute(schedule(ScheduleFrequency.ONCE), jan1, dec31)
        assertEquals(1, result.size)
        assertEquals(jan1, result[0].dueDate)
        assertEquals(0, result[0].index)
    }

    @Test
    fun `ONCE before window start returns empty`() {
        val past = LocalDateTime.of(2023, 6, 1, 0, 0)
        val result = useCase.execute(schedule(ScheduleFrequency.ONCE, startDate = past), jan1, dec31)
        assertTrue(result.isEmpty())
    }

    // ── DAILY ────────────────────────────────────────────────────────────────

    @Test
    fun `DAILY interval 1 generates one occurrence per day`() {
        val windowEnd = LocalDateTime.of(2024, 1, 5, 23, 59)
        val result = useCase.execute(schedule(ScheduleFrequency.DAILY), jan1, windowEnd)

        assertEquals(5, result.size)
        assertEquals(jan1, result[0].dueDate)
        assertEquals(LocalDateTime.of(2024, 1, 5, 0, 0), result[4].dueDate)
    }

    @Test
    fun `DAILY interval 3 generates every third day`() {
        val windowEnd = LocalDateTime.of(2024, 1, 10, 23, 59)
        val result = useCase.execute(schedule(ScheduleFrequency.DAILY, interval = 3), jan1, windowEnd)

        // Jan 1, Jan 4, Jan 7, Jan 10
        assertEquals(4, result.size)
        assertEquals(LocalDateTime.of(2024, 1, 4, 0, 0), result[1].dueDate)
        assertEquals(LocalDateTime.of(2024, 1, 7, 0, 0), result[2].dueDate)
        assertEquals(LocalDateTime.of(2024, 1, 10, 0, 0), result[3].dueDate)
    }

    // ── WEEKLY ───────────────────────────────────────────────────────────────

    @Test
    fun `WEEKLY interval 2 generates every two weeks`() {
        val windowEnd = LocalDateTime.of(2024, 2, 5, 23, 59)
        val result = useCase.execute(schedule(ScheduleFrequency.WEEKLY, interval = 2), jan1, windowEnd)

        // Jan 1, Jan 15, Jan 29
        assertEquals(3, result.size)
        assertEquals(LocalDateTime.of(2024, 1, 15, 0, 0), result[1].dueDate)
        assertEquals(LocalDateTime.of(2024, 1, 29, 0, 0), result[2].dueDate)
    }

    // ── MONTHLY ──────────────────────────────────────────────────────────────

    @Test
    fun `MONTHLY clamps day 31 to last day of short months`() {
        val startDate = LocalDateTime.of(2023, 1, 31, 0, 0)
        val windowEnd = LocalDateTime.of(2023, 4, 30, 23, 59)
        val result = useCase.execute(
            schedule(ScheduleFrequency.MONTHLY, startDate = startDate, dayOfMonth = 31),
            startDate, windowEnd
        )

        // Jan 31, Feb 28 (clamp), Mar 31, Apr 30 (clamp)
        assertEquals(4, result.size)
        assertEquals(LocalDateTime.of(2023, 2, 28, 0, 0), result[1].dueDate)
        assertEquals(LocalDateTime.of(2023, 3, 31, 0, 0), result[2].dueDate)
        assertEquals(LocalDateTime.of(2023, 4, 30, 0, 0), result[3].dueDate)
    }

    @Test
    fun `MONTHLY without dayOfMonth uses start day`() {
        val startDate = LocalDateTime.of(2024, 1, 15, 0, 0)
        val windowEnd = LocalDateTime.of(2024, 3, 31, 23, 59)
        val result = useCase.execute(schedule(ScheduleFrequency.MONTHLY, startDate = startDate), startDate, windowEnd)

        assertEquals(3, result.size)
        assertEquals(LocalDateTime.of(2024, 2, 15, 0, 0), result[1].dueDate)
        assertEquals(LocalDateTime.of(2024, 3, 15, 0, 0), result[2].dueDate)
    }

    // ── YEARLY ───────────────────────────────────────────────────────────────

    @Test
    fun `YEARLY interval 1 generates one occurrence per year`() {
        val windowEnd = LocalDateTime.of(2026, 12, 31, 23, 59)
        val result = useCase.execute(schedule(ScheduleFrequency.YEARLY), jan1, windowEnd)

        assertEquals(3, result.size)
        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0), result[0].dueDate)
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), result[1].dueDate)
        assertEquals(LocalDateTime.of(2026, 1, 1, 0, 0), result[2].dueDate)
    }

    // ── INSTALLMENTS ─────────────────────────────────────────────────────────

    @Test
    fun `installments cap limits occurrences regardless of window size`() {
        val result = useCase.execute(
            schedule(ScheduleFrequency.MONTHLY, installments = 3),
            jan1, dec31
        )
        assertEquals(3, result.size)
    }

    @Test
    fun `installments index is sequential from zero`() {
        val result = useCase.execute(
            schedule(ScheduleFrequency.MONTHLY, installments = 3),
            jan1, dec31
        )
        assertEquals(0, result[0].index)
        assertEquals(1, result[1].index)
        assertEquals(2, result[2].index)
    }

    // ── END DATE ─────────────────────────────────────────────────────────────

    @Test
    fun `endDate truncates occurrences before window end`() {
        val endDate = LocalDateTime.of(2024, 1, 3, 0, 0)
        val windowEnd = LocalDateTime.of(2024, 1, 10, 23, 59)
        val result = useCase.execute(
            schedule(ScheduleFrequency.DAILY, endDate = endDate),
            jan1, windowEnd
        )
        // Jan 1, Jan 2, Jan 3 only
        assertEquals(3, result.size)
        assertEquals(endDate, result[2].dueDate)
    }

    // ── PAID FILTERING ───────────────────────────────────────────────────────

    @Test
    fun `paid transactions are excluded from results`() {
        val jan2 = LocalDateTime.of(2024, 1, 2, 0, 0)
        val windowEnd = LocalDateTime.of(2024, 1, 3, 23, 59)
        val s = schedule(ScheduleFrequency.DAILY)

        val paidTx = mockk<Transaction> {
            every { scheduleId } returns s.id
            every { originalDueDate } returns jan2
        }

        val result = useCase.execute(s, jan1, windowEnd, listOf(paidTx))

        // Jan 1, [Jan 2 paid], Jan 3 → 2 occurrences
        assertEquals(2, result.size)
        assertTrue(result.none { it.dueDate == jan2 })
    }

    @Test
    fun `paid gap does not break sequential index`() {
        val jan2 = LocalDateTime.of(2024, 1, 2, 0, 0)
        val windowEnd = LocalDateTime.of(2024, 1, 3, 23, 59)
        val s = schedule(ScheduleFrequency.DAILY)

        val paidTx = mockk<Transaction> {
            every { scheduleId } returns s.id
            every { originalDueDate } returns jan2
        }

        val result = useCase.execute(s, jan1, windowEnd, listOf(paidTx))

        assertEquals(0, result[0].index)  // Jan 1
        assertEquals(2, result[1].index)  // Jan 3 (index 1 = Jan 2 was paid)
    }

    @Test
    fun `paid transaction from different schedule is not excluded`() {
        val jan2 = LocalDateTime.of(2024, 1, 2, 0, 0)
        val windowEnd = LocalDateTime.of(2024, 1, 3, 23, 59)
        val s = schedule(ScheduleFrequency.DAILY)

        val paidTxOtherSchedule = mockk<Transaction> {
            every { scheduleId } returns 999L  // different schedule
            every { originalDueDate } returns jan2
        }

        val result = useCase.execute(s, jan1, windowEnd, listOf(paidTxOtherSchedule))

        assertEquals(3, result.size)  // Jan 1, Jan 2, Jan 3 all present
    }
}

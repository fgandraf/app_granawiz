package application.schedule.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Schedule
import domain.entity.Transaction
import domain.enums.ScheduleFrequency
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MarkAsPaidUseCaseTest {

    private val repo = mockk<ITransactionRepository>(relaxed = true)
    private val useCase = MarkAsPaidUseCase(repo)

    private val dueDate = LocalDateTime.of(2024, 3, 15, 0, 0)

    private fun schedule(installments: Int? = null) = Schedule().copy(
        frequency = ScheduleFrequency.MONTHLY,
        startDate = dueDate,
        installments = installments,
    )

    @Test
    fun `with occurrenceIndex uses it to build installment string`() {
        val sched = schedule(installments = 6)
        val slot = slot<Transaction>()
        every { repo.insert(capture(slot)) } returns Unit

        useCase.execute(sched, dueDate, occurrenceIndex = 2)

        assertEquals("3/6", slot.captured.installment)
    }

    @Test
    fun `without occurrenceIndex counts existing transactions`() {
        val sched = schedule(installments = 12)
        every { repo.findByScheduleId(sched.id) } returns listOf(mockk(), mockk())
        val slot = slot<Transaction>()
        every { repo.insert(capture(slot)) } returns Unit

        useCase.execute(sched, dueDate)

        assertEquals("3/12", slot.captured.installment)
    }

    @Test
    fun `no installments defaults total to 1`() {
        val sched = schedule(installments = null)
        val slot = slot<Transaction>()
        every { repo.insert(capture(slot)) } returns Unit

        useCase.execute(sched, dueDate, occurrenceIndex = 0)

        assertEquals("1/1", slot.captured.installment)
    }

    @Test
    fun `creates transaction with correct schedule fields`() {
        val sched = schedule(installments = 3)
        val slot = slot<Transaction>()
        every { repo.insert(capture(slot)) } returns Unit

        useCase.execute(sched, dueDate, occurrenceIndex = 0)

        assertEquals(sched.id, slot.captured.scheduleId)
        assertEquals(dueDate, slot.captured.date)
        assertEquals(dueDate, slot.captured.originalDueDate)
        assertEquals(sched.balance, slot.captured.balance)
        assertEquals(sched.type, slot.captured.type)
    }
}

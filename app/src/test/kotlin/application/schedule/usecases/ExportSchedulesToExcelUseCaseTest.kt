package application.schedule.usecases

import domain.entity.Schedule
import domain.enums.ScheduleFrequency
import domain.enums.TransactionType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime

class ExportSchedulesToExcelUseCaseTest {

    private val useCase = ExportSchedulesToExcelUseCase()

    private fun occurrence(month: Int, day: Int = 15) = ScheduleOccurrence(
        schedule = Schedule().copy(
            frequency = ScheduleFrequency.MONTHLY,
            startDate = LocalDateTime.of(2024, month, day, 0, 0),
            balance = -100.0,
            type = TransactionType.EXPENSE,
            description = "Test Expense",
        ),
        dueDate = LocalDateTime.of(2024, month, day, 0, 0),
        index = 0,
    )

    private fun tempFile() = File.createTempFile("granawiz_test_", ".xlsx").also { it.deleteOnExit() }

    @Test
    fun `creates a non-empty xlsx file`() {
        val file = tempFile()
        useCase.execute(listOf(occurrence(1)), file)
        assertTrue(file.exists())
        assertTrue(file.length() > 0)
    }

    @Test
    fun `workbook has sheet named Agendamentos`() {
        val file = tempFile()
        useCase.execute(listOf(occurrence(1)), file)
        val wb = XSSFWorkbook(file)
        assertNotNull(wb.getSheet("Agendamentos"))
        wb.close()
    }

    @Test
    fun `empty occurrences list creates empty but valid workbook`() {
        val file = tempFile()
        useCase.execute(emptyList(), file)
        assertTrue(file.exists())
        val wb = XSSFWorkbook(file)
        assertNotNull(wb.getSheet("Agendamentos"))
        wb.close()
    }

    @Test
    fun `occurrences grouped by month produce separate month sections`() {
        val file = tempFile()
        useCase.execute(listOf(occurrence(1), occurrence(3)), file)
        val wb = XSSFWorkbook(file)
        val sheet = wb.getSheet("Agendamentos")
        assertTrue(sheet.physicalNumberOfRows > 2)
        wb.close()
    }
}

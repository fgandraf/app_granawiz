package application.schedule.usecases

import domain.enums.ScheduleFrequency
import domain.enums.TransactionType
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import utils.brMoney
import java.io.File
import java.time.Month

class ExportSchedulesToExcelUseCase {

    private val monthNames = mapOf(
        Month.JANUARY to "Janeiro",
        Month.FEBRUARY to "Fevereiro",
        Month.MARCH to "Março",
        Month.APRIL to "Abril",
        Month.MAY to "Maio",
        Month.JUNE to "Junho",
        Month.JULY to "Julho",
        Month.AUGUST to "Agosto",
        Month.SEPTEMBER to "Setembro",
        Month.OCTOBER to "Outubro",
        Month.NOVEMBER to "Novembro",
        Month.DECEMBER to "Dezembro"
    )

    private val frequencyLabels = mapOf(
        ScheduleFrequency.ONCE to "Uma vez",
        ScheduleFrequency.DAILY to "Diária",
        ScheduleFrequency.WEEKLY to "Semanal",
        ScheduleFrequency.MONTHLY to "Mensal",
        ScheduleFrequency.YEARLY to "Anual"
    )

    private val columnHeaders = listOf(
        "Vencimento", "Descrição", "Parte", "Categoria", "Subcategoria", "Tags", "Conta", "Tipo", "Frequência", "Valor"
    )

    fun execute(occurrences: List<ScheduleOccurrence>, file: File) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Agendamentos")
        val fmt = workbook.createDataFormat()

        val monthHeaderStyle = workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            setFont(workbook.createFont().also { it.bold = true; it.fontHeightInPoints = 13 })
        }
        val colHeaderStyle = workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.GREY_40_PERCENT.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            setFont(workbook.createFont().also { it.bold = true; it.fontHeightInPoints = 11 })
        }
        val dateStyle = workbook.createCellStyle().apply {
            dataFormat = fmt.getFormat("dd/MM/yyyy")
        }
        val currencyStyle = workbook.createCellStyle().apply {
            dataFormat = fmt.getFormat("\"R$ \"#,##0.00;[Red]-\"R$ \"#,##0.00")
        }
        val totalStyle = workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            setFont(workbook.createFont().also { it.italic = true })
        }

        val grouped = occurrences.groupBy { it.dueDate.month }
        var rowIndex = 0

        for ((month, monthOccurrences) in grouped) {
            val sorted = monthOccurrences.sortedBy { it.dueDate }

            val headerRow = sheet.createRow(rowIndex)
            for (col in 0..9) {
                headerRow.createCell(col).cellStyle = monthHeaderStyle
            }
            headerRow.getCell(0).setCellValue(monthNames[month] ?: "Mês desconhecido")
            sheet.addMergedRegion(CellRangeAddress(rowIndex, rowIndex, 0, 9))
            rowIndex++

            val colRow = sheet.createRow(rowIndex++)
            columnHeaders.forEachIndexed { i, label ->
                colRow.createCell(i).also {
                    it.setCellValue(label)
                    it.cellStyle = colHeaderStyle
                }
            }

            for (occ in sorted) {
                val s = occ.schedule
                val row = sheet.createRow(rowIndex++)
                row.createCell(0).also { it.setCellValue(occ.dueDate); it.cellStyle = dateStyle }
                row.createCell(1).setCellValue(s.description)
                row.createCell(2).setCellValue(s.party.name)
                row.createCell(3).setCellValue(s.category.name)
                row.createCell(4).setCellValue(s.subcategory?.name ?: "")
                row.createCell(5).setCellValue(s.tags?.joinToString(", ") { it.name } ?: "")
                row.createCell(6).setCellValue(s.account.name)
                row.createCell(7).setCellValue(when (s.type) {
                    TransactionType.GAIN -> "Receita"
                    TransactionType.EXPENSE -> "Despesa"
                    TransactionType.NEUTRAL -> "Neutro"
                })
                row.createCell(8).setCellValue(frequencyLabels[s.frequency] ?: s.frequency.name)
                row.createCell(9).also { it.setCellValue(s.balance); it.cellStyle = currencyStyle }
            }

            val positive = sorted.filter { it.schedule.balance >= 0 }.sumOf { it.schedule.balance }
            val negative = sorted.filter { it.schedule.balance < 0 }.sumOf { it.schedule.balance } * -1
            val totalsRow = sheet.createRow(rowIndex)
            for (col in 0..9) totalsRow.createCell(col).cellStyle = totalStyle
            totalsRow.getCell(0).setCellValue(
                "Receitas: ${brMoney.format(positive)}   Despesas: ${brMoney.format(negative)}"
            )
            sheet.addMergedRegion(CellRangeAddress(rowIndex, rowIndex, 0, 9))
            rowIndex++

            sheet.createRow(rowIndex++)
        }

        for (i in 0..9) sheet.autoSizeColumn(i)

        file.outputStream().use { workbook.write(it) }
        workbook.close()
    }
}

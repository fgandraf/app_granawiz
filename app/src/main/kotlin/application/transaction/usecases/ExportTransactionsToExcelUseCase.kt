package application.transaction.usecases

import domain.entity.Transaction
import domain.enums.TransactionType
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import utils.brMoney
import java.io.File
import java.time.Month

class ExportTransactionsToExcelUseCase {

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

    private val columnHeaders = listOf(
        "Data", "Descrição", "Parte", "Categoria", "Subcategoria", "Tags", "Conta", "Tipo", "Valor"
    )

    fun execute(transactions: List<Transaction>, file: File) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Transações")
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

        val grouped = transactions.groupBy { it.date.month }
        var rowIndex = 0

        for ((month, monthTransactions) in grouped) {
            val sorted = monthTransactions.sortedBy { it.date }

            // Month header — merged across all 9 columns, style applied to each cell
            val headerRow = sheet.createRow(rowIndex)
            for (col in 0..8) {
                headerRow.createCell(col).cellStyle = monthHeaderStyle
            }
            headerRow.getCell(0).setCellValue(monthNames[month] ?: "Mês desconhecido")
            sheet.addMergedRegion(CellRangeAddress(rowIndex, rowIndex, 0, 8))
            rowIndex++

            // Column header row
            val colRow = sheet.createRow(rowIndex++)
            columnHeaders.forEachIndexed { i, label ->
                colRow.createCell(i).also {
                    it.setCellValue(label)
                    it.cellStyle = colHeaderStyle
                }
            }

            // Data rows
            for (t in sorted) {
                val row = sheet.createRow(rowIndex++)
                row.createCell(0).also { it.setCellValue(t.date); it.cellStyle = dateStyle }
                row.createCell(1).setCellValue(t.description)
                row.createCell(2).setCellValue(t.party.name)
                row.createCell(3).setCellValue(t.category.name)
                row.createCell(4).setCellValue(t.subcategory?.name ?: "")
                row.createCell(5).setCellValue(t.tags?.joinToString(", ") { it.name } ?: "")
                row.createCell(6).setCellValue(t.account.name)
                row.createCell(7).setCellValue(when (t.type) {
                    TransactionType.GAIN -> "Receita"
                    TransactionType.EXPENSE -> "Despesa"
                    TransactionType.NEUTRAL -> "Neutro"
                })
                row.createCell(8).also { it.setCellValue(t.balance); it.cellStyle = currencyStyle }
            }

            // Totals row
            val positive = sorted.filter { it.balance >= 0 }.sumOf { it.balance }
            val negative = sorted.filter { it.balance < 0 }.sumOf { it.balance } * -1
            val totalsRow = sheet.createRow(rowIndex)
            for (col in 0..8) totalsRow.createCell(col).cellStyle = totalStyle
            totalsRow.getCell(0).setCellValue(
                "Receitas: ${brMoney.format(positive)}   Despesas: ${brMoney.format(negative)}"
            )
            sheet.addMergedRegion(CellRangeAddress(rowIndex, rowIndex, 0, 8))
            rowIndex++

            // Blank separator
            sheet.createRow(rowIndex++)
        }

        for (i in 0..8) sheet.autoSizeColumn(i)

        file.outputStream().use { workbook.write(it) }
        workbook.close()
    }
}

package application.transaction.usecases

import domain.entity.Transaction
import domain.enums.TransactionType
import java.io.File
import java.time.format.DateTimeFormatter
import java.util.Locale

class ExportTransactionsToCsvUseCase {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val delimiter = ';'

    private val headers = listOf(
        "Data", "Descricao", "Documento", "Tipo", "Valor",
        "Categoria", "Recebedor/Pagador", "Parcela", "Conta", "Etiquetas"
    )

    fun execute(transactions: List<Transaction>, file: File) {
        file.bufferedWriter(Charsets.UTF_8).use { writer ->
            writer.append(headers.joinToString(delimiter.toString()))
            writer.append('\n')

            transactions.sortedBy { it.date }.forEach { t ->
                val categoria = t.subcategory?.let { "${t.category.name}/${it.name}" } ?: t.category.name
                val tags = t.tags?.joinToString(", ") { it.name } ?: ""
                val tipo = when (t.type) {
                    TransactionType.EXPENSE -> "Debito"
                    TransactionType.GAIN, TransactionType.NEUTRAL -> "Credito"
                }
                val valor = String.format(Locale.US, "%.2f", t.balance)

                val row = listOf(
                    t.date.format(dateFormatter),
                    t.description,
                    "",
                    tipo,
                    valor,
                    categoria,
                    t.party.name,
                    t.installment,
                    t.account.name,
                    tags,
                ).joinToString(delimiter.toString()) { escape(it) }

                writer.append(row)
                writer.append('\n')
            }
        }
    }

    private fun escape(field: String): String {
        val needsQuoting = field.any { it == delimiter || it == ',' || it == '"' || it == '\n' || it == '\r' }
        return if (needsQuoting) "\"${field.replace("\"", "\"\"")}\"" else field
    }
}

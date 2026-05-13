package application.importStatement.usecases

import application.importStatement.LogLevel
import domain.enums.TransactionType
import domain.structs.ParsedEntry
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ParseCsvFileUseCase {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun execute(file: File, onLog: (String, LogLevel) -> Unit): List<ParsedEntry> = try {
        onLog("Lendo conteúdo do arquivo CSV...", LogLevel.INFO)
        val lines = file.readLines(Charsets.UTF_8).filter { it.isNotBlank() }
        if (lines.size < 2) {
            onLog("Arquivo CSV vazio ou sem registros.", LogLevel.ERROR)
            return emptyList()
        }

        val delimiter = if (lines.first().contains(';')) ';' else ','
        val headerMap = parseCsvLine(lines.first(), delimiter).mapIndexed { i, col ->
            normalize(col) to i
        }.toMap()

        fun List<String>.col(vararg names: String): String? =
            names.firstNotNullOfOrNull {
                headerMap[it]?.let { idx ->
                    getOrNull(idx)?.trim()?.takeIf { it.isNotBlank() }
                }
            }

        val dataLines = lines.drop(1)
        onLog("Carregando registros em memória...", LogLevel.INFO)

        val hasPartyCol = names("recebedorpagador", "recebedor", "pagador").any { it in headerMap }

        val entries = mutableListOf<ParsedEntry>()
        var skipped = 0

        for ((index, line) in dataLines.withIndex()) {
            val cols = parseCsvLine(line, delimiter)

            try {
                val dateStr = cols.col("data") ?: run {
                    onLog("Linha ${index + 2} ignorada: coluna 'Data' ausente.", LogLevel.WARN)
                    skipped++
                    continue
                }
                val descricao = cols.col("descricao", "descrição") ?: ""
                val documento = cols.col("documento")
                val tipoRaw = cols.col("tipo")?.lowercase() ?: ""
                val valorStr = cols.col("valor") ?: run {
                    onLog("Linha ${index + 2} ignorada: coluna 'Valor' ausente.", LogLevel.WARN)
                    skipped++
                    continue
                }

                val valorRaw = valorStr.replace(",", ".").toDoubleOrNull() ?: run {
                    onLog("Linha ${index + 2} ignorada: valor inválido '$valorStr'.", LogLevel.WARN)
                    skipped++
                    continue
                }

                val date = LocalDate.parse(dateStr, dateFormatter).atStartOfDay()

                val type = when {
                    tipoRaw.startsWith("c") -> TransactionType.GAIN
                    tipoRaw.startsWith("d") -> TransactionType.EXPENSE
                    else -> if (valorRaw >= 0) TransactionType.GAIN else TransactionType.EXPENSE
                }
                val balance = when {
                    type == TransactionType.EXPENSE && valorRaw > 0 -> -valorRaw
                    type == TransactionType.GAIN && valorRaw < 0 -> -valorRaw
                    else -> valorRaw
                }

                val partyName = if (hasPartyCol)
                    cols.col("recebedorpagador", "recebedor", "pagador")
                else null
                val rawCounterparty = partyName ?: descricao
                val description = descricao

                val categoria = cols.col("categoria")
                val parcela = cols.col("parcela")?.takeIf { it.matches(Regex("""\d+/\d+""")) }

                entries.add(
                    ParsedEntry(
                        fitId = documento,
                        date = date,
                        rawCounterpartyName = rawCounterparty,
                        description = description,
                        balance = balance,
                        type = type,
                        party = null,
                        needsNewParty = true,
                        category = null,
                        customCategoryText = categoria,
                        installment = parcela ?: "1/1",
                    )
                )
            } catch (e: Exception) {
                onLog("Linha ${index + 2} ignorada: ${e.message ?: "formato inválido"}.", LogLevel.WARN)
                skipped++
            }
        }

        onLog(
            "${entries.size} transações encontradas${if (skipped > 0) ", $skipped linha(s) ignorada(s)" else ""}.",
            LogLevel.OK
        )
        entries
    } catch (e: Exception) {
        onLog("Erro ao ler o arquivo CSV: ${e.message ?: "erro desconhecido"}", LogLevel.ERROR)
        emptyList()
    }

    private fun parseCsvLine(line: String, delimiter: Char): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            when (val c = line[i]) {
                '"' -> if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                    current.append('"'); i++
                } else {
                    inQuotes = !inQuotes
                }
                delimiter -> if (inQuotes) current.append(c) else { result.add(current.toString().trim()); current.clear() }
                else -> current.append(c)
            }
            i++
        }
        result.add(current.toString().trim())
        return result
    }

    private fun normalize(col: String): String =
        col.trim().trimStart('﻿').lowercase()
            .replace('á', 'a').replace('ã', 'a').replace('â', 'a').replace('à', 'a')
            .replace('é', 'e').replace('ê', 'e')
            .replace('í', 'i')
            .replace('ó', 'o').replace('ô', 'o')
            .replace('ú', 'u').replace('ü', 'u')
            .replace('ç', 'c')
            .replace("/", "")
            .replace(" ", "")

    private fun names(vararg keys: String) = keys.toSet()
}

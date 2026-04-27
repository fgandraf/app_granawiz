package application.importStatement.usecases

import application.importStatement.LogLevel
import com.webcohesion.ofx4j.domain.data.MessageSetType
import com.webcohesion.ofx4j.domain.data.ResponseEnvelope
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet
import com.webcohesion.ofx4j.domain.data.common.Transaction as OfxTxn
import com.webcohesion.ofx4j.domain.data.creditcard.CreditCardResponseMessageSet
import com.webcohesion.ofx4j.io.AggregateUnmarshaller
import domain.enums.TransactionType
import domain.structs.ParsedEntry
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId

class ParseOfxFileUseCase {

    fun execute(file: File, onLog: (String, LogLevel) -> Unit): List<ParsedEntry> = try {
        onLog("Lendo conteúdo do arquivo...", LogLevel.INFO)
        val unmarshaller = AggregateUnmarshaller(ResponseEnvelope::class.java)
        val envelope: ResponseEnvelope = file.bufferedReader().use { unmarshaller.unmarshal(it) }

        onLog("Carregando registros em memória...", LogLevel.INFO)

        val ofxTxns: List<OfxTxn> = buildList {
            // Banking statements
            (envelope.getMessageSet(MessageSetType.banking) as? BankingResponseMessageSet)
                ?.statementResponses?.forEach { wrapper ->
                    wrapper.message?.transactionList?.transactions?.let { addAll(it) }
                }
            // Credit card statements
            (envelope.getMessageSet(MessageSetType.creditcard) as? CreditCardResponseMessageSet)
                ?.statementResponses?.forEach { wrapper ->
                    wrapper.message?.transactionList?.transactions?.let { addAll(it) }
                }
        }

        onLog("${ofxTxns.size} transações encontradas.", LogLevel.OK)

        ofxTxns.map { ofx ->
            val amount = (ofx.amount ?: java.math.BigDecimal.ZERO).toDouble()
            val type = when {
                amount > 0 -> TransactionType.GAIN
                amount < 0 -> TransactionType.EXPENSE
                else -> TransactionType.NEUTRAL
            }
            val rawName = (ofx.name?.takeIf { it.isNotBlank() } ?: ofx.memo ?: "").trim()
            val date = ofx.datePosted
                ?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
                ?: LocalDateTime.now()

            ParsedEntry(
                fitId = ofx.id,
                date = date,
                rawCounterpartyName = rawName,
                description = (ofx.memo?.takeIf { it.isNotBlank() } ?: rawName),
                balance = amount,
                type = type,
                party = null,
                needsNewParty = true,
                category = null,
            )
        }
    } catch (e: Exception) {
        onLog("Erro ao ler o arquivo: ${e.message ?: "erro desconhecido"}", LogLevel.ERROR)
        //onLog("O arquivo não parece ser um extrato OFX válido. Verifique se foi exportado corretamente pelo seu banco.", LogLevel.ERROR)
        emptyList()
    }
}

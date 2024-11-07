package application.importStatement.usecases

import application.importStatement.LogLevel
import domain.enums.TransactionType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class ParseCsvFileUseCaseTest {

    private val useCase = ParseCsvFileUseCase()
    private val log: (String, LogLevel) -> Unit = { _, _ -> }

    private fun csv(content: String): File =
        File.createTempFile("granawiz_test_", ".csv").also {
            it.writeText(content.trimIndent(), Charsets.UTF_8)
            it.deleteOnExit()
        }

    // ── DELIMITER DETECTION ───────────────────────────────────────────────────

    @Test
    fun `semicolon delimiter is auto-detected`() {
        val file = csv("Data;Tipo;Valor;Descricao\n01/01/2024;C;100,00;Salario")
        val result = useCase.execute(file, log)
        assertEquals(1, result.size)
        assertEquals(100.0, result[0].balance, 0.001)
    }

    @Test
    fun `comma delimiter is auto-detected`() {
        val file = csv("Data,Tipo,Valor,Descricao\n15/06/2024,D,75.50,Mercado")
        val result = useCase.execute(file, log)
        assertEquals(1, result.size)
        assertEquals(-75.50, result[0].balance, 0.001)
    }

    // ── TRANSACTION TYPE ──────────────────────────────────────────────────────

    @Test
    fun `tipo starting with C maps to GAIN`() {
        val file = csv("Data;Tipo;Valor;Descricao\n01/01/2024;credito;200,00;Entrada")
        val result = useCase.execute(file, log)
        assertEquals(TransactionType.GAIN, result[0].type)
        assertEquals(200.0, result[0].balance, 0.001)
    }

    @Test
    fun `tipo starting with D maps to EXPENSE with negated balance`() {
        val file = csv("Data;Tipo;Valor;Descricao\n01/01/2024;debito;50,00;Saida")
        val result = useCase.execute(file, log)
        assertEquals(TransactionType.EXPENSE, result[0].type)
        assertEquals(-50.0, result[0].balance, 0.001)
    }

    @Test
    fun `uppercase C in tipo maps to GAIN`() {
        val file = csv("Data;Tipo;Valor;Descricao\n01/01/2024;C;80,00;Pix")
        val result = useCase.execute(file, log)
        assertEquals(TransactionType.GAIN, result[0].type)
    }

    @Test
    fun `uppercase D in tipo maps to EXPENSE`() {
        val file = csv("Data;Tipo;Valor;Descricao\n01/01/2024;D;30,00;Conta")
        val result = useCase.execute(file, log)
        assertEquals(TransactionType.EXPENSE, result[0].type)
    }

    // ── TYPE INFERENCE FROM AMOUNT SIGN ───────────────────────────────────────

    @Test
    fun `positive amount without tipo is inferred as GAIN`() {
        val file = csv("Data;Valor;Descricao\n01/01/2024;300,00;Entrada sem tipo")
        val result = useCase.execute(file, log)
        assertEquals(TransactionType.GAIN, result[0].type)
        assertEquals(300.0, result[0].balance, 0.001)
    }

    @Test
    fun `negative amount without tipo is inferred as EXPENSE`() {
        val file = csv("Data;Valor;Descricao\n01/01/2024;-80,00;Saida sem tipo")
        val result = useCase.execute(file, log)
        assertEquals(TransactionType.EXPENSE, result[0].type)
        assertEquals(-80.0, result[0].balance, 0.001)
    }

    // ── BALANCE SIGN CORRECTION ───────────────────────────────────────────────

    @Test
    fun `EXPENSE with positive value has balance negated`() {
        val file = csv("Data;Tipo;Valor;Descricao\n01/01/2024;D;99,99;Conta")
        val result = useCase.execute(file, log)
        assertEquals(-99.99, result[0].balance, 0.001)
    }

    @Test
    fun `GAIN with negative value has balance corrected to positive`() {
        val file = csv("Data;Tipo;Valor;Descricao\n01/01/2024;C;-50,00;Estorno")
        val result = useCase.execute(file, log)
        assertEquals(TransactionType.GAIN, result[0].type)
        assertEquals(50.0, result[0].balance, 0.001)
    }

    // ── SKIPPING / ERROR HANDLING ─────────────────────────────────────────────

    @Test
    fun `file with only header returns empty list`() {
        val file = csv("Data;Tipo;Valor;Descricao")
        val result = useCase.execute(file, log)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `completely empty file returns empty list`() {
        val file = File.createTempFile("granawiz_test_", ".csv").also {
            it.writeText("")
            it.deleteOnExit()
        }
        val result = useCase.execute(file, log)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `row with invalid amount is skipped, valid rows are kept`() {
        val file = csv("""
            Data;Tipo;Valor;Descricao
            01/01/2024;C;abc;Invalido
            02/01/2024;C;10,00;Valido
        """)
        val result = useCase.execute(file, log)
        assertEquals(1, result.size)
        assertEquals(10.0, result[0].balance, 0.001)
    }

    @Test
    fun `row without Data column is skipped`() {
        val file = csv("Tipo;Valor;Descricao\nC;100,00;Sem data")
        val result = useCase.execute(file, log)
        assertTrue(result.isEmpty())
    }

    // ── COLUMN MAPPING ────────────────────────────────────────────────────────

    @Test
    fun `accented header Descricao is normalised and matched`() {
        val file = csv("Data;Tipo;Valor;Descrição\n01/01/2024;C;50,00;Pix recebido")
        val result = useCase.execute(file, log)
        assertEquals(1, result.size)
        assertEquals("Pix recebido", result[0].description)
    }

    @Test
    fun `Parcela column sets installment field`() {
        val file = csv("Data;Tipo;Valor;Descricao;Parcela\n01/01/2024;D;100,00;Compra;2/12")
        val result = useCase.execute(file, log)
        assertEquals("2/12", result[0].installment)
    }

    @Test
    fun `row without Parcela defaults to 1 of 1`() {
        val file = csv("Data;Tipo;Valor;Descricao\n01/01/2024;C;50,00;Pix")
        val result = useCase.execute(file, log)
        assertEquals("1/1", result[0].installment)
    }

    @Test
    fun `Documento column maps to fitId`() {
        val file = csv("Data;Tipo;Valor;Descricao;Documento\n01/01/2024;C;50,00;Entrada;DOC-123")
        val result = useCase.execute(file, log)
        assertEquals("DOC-123", result[0].fitId)
    }

    // ── MULTIPLE ROWS ─────────────────────────────────────────────────────────

    @Test
    fun `multiple valid rows are all parsed`() {
        val file = csv("""
            Data;Tipo;Valor;Descricao
            01/01/2024;C;100,00;Entrada1
            02/01/2024;D;50,00;Saida1
            03/01/2024;C;200,00;Entrada2
        """)
        val result = useCase.execute(file, log)
        assertEquals(3, result.size)
    }

    @Test
    fun `rawCounterpartyName falls back to description when no party column`() {
        val file = csv("Data;Tipo;Valor;Descricao\n01/01/2024;C;100,00;Salario empresa")
        val result = useCase.execute(file, log)
        assertEquals("Salario empresa", result[0].rawCounterpartyName)
    }

    @Test
    fun `party column takes precedence over description for rawCounterpartyName`() {
        val file = csv("Data;Tipo;Valor;Descricao;Recebedor\n01/01/2024;C;100,00;PIX RECEBIDO;Empresa XYZ")
        val result = useCase.execute(file, log)
        assertEquals("Empresa XYZ", result[0].rawCounterpartyName)
    }
}

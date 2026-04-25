package view.modules.importStatement.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.shared.DefaultButton
import view.shared.TextH1
import view.shared.TextNormal
import java.io.File

private val WarningOrange = Color(0xFFE67E22)

private enum class LogLevel { INFO, OK, WARN, ERROR }

private data class LogLine(val level: LogLevel, val text: String)

private fun buildLogLines(file: File?): List<LogLine> = buildList {
    add(LogLine(LogLevel.INFO, "Iniciando análise do arquivo..."))
    if (file == null) {
        add(LogLine(LogLevel.ERROR, "Nenhum arquivo selecionado."))
        return@buildList
    }
    add(LogLine(LogLevel.INFO,  "Arquivo: ${file.name}"))
    add(LogLine(LogLevel.INFO,  "Lendo conteúdo do arquivo..."))
    add(LogLine(LogLevel.INFO,  "Carregando registros em memória..."))
    add(LogLine(LogLevel.OK,    "8 transações encontradas."))
    add(LogLine(LogLevel.INFO,  "Verificando inconsistências de formato..."))
    add(LogLine(LogLevel.OK,    "Nenhuma inconsistência de formato encontrada."))
    add(LogLine(LogLevel.INFO,  "Verificando duplicidades com a base de dados..."))
    add(LogLine(LogLevel.WARN,  "2 possíveis duplicatas detectadas."))
    add(LogLine(LogLevel.INFO,  "Análise concluída. Revise os dados na próxima etapa."))
}

@Composable
fun WizardStepTwo(
    selectedFile: File?,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    val logLines = buildLogLines(selectedFile)

    Column(modifier = Modifier.fillMaxSize()) {

        TextH1(text = "Analisando arquivo")
        Spacer(Modifier.height(8.dp))
        TextNormal(text = "Verificando transações, inconsistências e duplicidades antes da conciliação.")

        Spacer(Modifier.height(16.dp))

        val listState = rememberLazyListState()
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(6.dp))
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colors.background)
                .padding(12.dp)
        ) {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                items(logLines) { line ->
                    TextNormal(
                        text = line.text,
                        color = when (line.level) {
                            LogLevel.OK    -> MaterialTheme.colors.onPrimary
                            LogLevel.WARN  -> WarningOrange
                            LogLevel.ERROR -> MaterialTheme.colors.onError
                            LogLevel.INFO  -> MaterialTheme.colors.primary.copy(alpha = 0.8f)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        fontSize = 11.sp
                    )
                }
            }
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(listState),
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        Spacer(Modifier.height(16.dp))

        Divider(modifier = Modifier.padding(bottom = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DefaultButton(
                modifier = Modifier.width(160.dp),
                text = "Voltar",
                color = MaterialTheme.colors.onSurface,
                textColor = if (MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.secondary,
                onClick = onBack
            )
            DefaultButton(
                modifier = Modifier.width(160.dp),
                text = "Próximo",
                textColor = if (MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.secondary,
                onClick = onNext
            )
        }
    }
}

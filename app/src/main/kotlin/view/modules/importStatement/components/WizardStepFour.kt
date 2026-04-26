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
import view.shared.DefaultButton
import view.shared.TextH1
import view.shared.TextNormal

private val mockLogLines = listOf(
    "Iniciando importação...",
    "Lendo arquivo de extrato...",
    "Aguardando processamento real.",
    "Esta etapa será implementada em breve.",
)

@Composable
fun WizardStepFour(onFinish: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {

        TextH1(text = "Processando importação")
        Spacer(Modifier.height(8.dp))
        TextNormal(text = "Acompanhe o progresso da importação abaixo.")

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
                items(mockLogLines) { line ->
                    TextNormal(
                        text = line,
                        color = MaterialTheme.colors.primary.copy(alpha = 0.8f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        fontSize = androidx.compose.ui.unit.TextUnit(
                            11f,
                            androidx.compose.ui.unit.TextUnitType.Sp
                        )
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

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            DefaultButton(
                modifier = Modifier.width(200.dp),
                text = "Concluir",
                textColor = if (MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.secondary,
                onClick = onFinish
            )
        }
    }
}

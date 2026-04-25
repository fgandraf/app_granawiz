package view.modules.importStatement.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.enums.TransactionType
import view.shared.DefaultButton
import view.shared.TextH1
import view.shared.TextH4
import view.shared.TextNormal

private data class MockEntry(
    val type: TransactionType,
    val date: String,
    val party: String,
    val description: String,
    val value: String,
    val category: String,
)

private val mockEntries = listOf(
    MockEntry(TransactionType.EXPENSE, "01/04/2025", "Supermercado Extra", "Compras do mês", "R$ 230,50", "Alimentação"),
    MockEntry(TransactionType.GAIN,    "02/04/2025", "Empresa LTDA",       "Salário",        "R$ 5.000,00", "Rendimentos"),
    MockEntry(TransactionType.EXPENSE, "03/04/2025", "Netflix",            "Assinatura",     "R$ 55,90", "Lazer"),
    MockEntry(TransactionType.EXPENSE, "05/04/2025", "Farmácia Popular",   "Medicamentos",   "R$ 45,00", "Saúde"),
    MockEntry(TransactionType.GAIN,    "07/04/2025", "Cliente XYZ",        "Freelance",      "R$ 800,00", "Rendimentos"),
    MockEntry(TransactionType.EXPENSE, "08/04/2025", "Posto Ipiranga",     "Abastecimento",  "R$ 180,00", "Transporte"),
    MockEntry(TransactionType.EXPENSE, "10/04/2025", "Restaurante Central","Almoço",         "R$ 92,00", "Alimentação"),
    MockEntry(TransactionType.EXPENSE, "12/04/2025", "Renner",             "Roupas",         "R$ 320,00", "Vestuário"),
)

@Composable
fun WizardStepTwo(onBack: () -> Unit, onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {

        TextH1(text = "Revise a conciliação")
        Spacer(Modifier.height(8.dp))
        TextNormal(text = "Ajuste os dados antes de continuar.")

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(6.dp))
                .clip(RoundedCornerShape(6.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.07f))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Box(modifier = Modifier.width(30.dp))
                TextH4(text = "Data", modifier = Modifier.weight(1.5f))
                TextH4(text = "Pagador/Beneficiário", modifier = Modifier.weight(2.5f))
                TextH4(text = "Descrição", modifier = Modifier.weight(2.5f))
                TextH4(text = "Valor", modifier = Modifier.weight(1.5f))
                TextH4(text = "Categoria sugerida", modifier = Modifier.weight(2f))
            }

            Divider()

            val listState = rememberLazyListState()
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    items(mockEntries.size) { i ->
                        val entry = mockEntries[i]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.width(30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (entry.type == TransactionType.GAIN) MaterialTheme.colors.onPrimary
                                            else MaterialTheme.colors.onError
                                        )
                                )
                            }
                            TextNormal(text = entry.date, modifier = Modifier.weight(1.5f))
                            TextNormal(text = entry.party, modifier = Modifier.weight(2.5f))
                            TextNormal(text = entry.description, modifier = Modifier.weight(2.5f))
                            TextNormal(
                                text = if (entry.type == TransactionType.EXPENSE) "- ${entry.value}" else entry.value,
                                color = if (entry.type == TransactionType.GAIN) MaterialTheme.colors.onPrimary
                                        else MaterialTheme.colors.onError,
                                modifier = Modifier.weight(1.5f)
                            )
                            TextNormal(
                                text = entry.category,
                                color = MaterialTheme.colors.primary.copy(alpha = 0.7f),
                                modifier = Modifier.weight(2f)
                            )
                        }
                        if (i < mockEntries.lastIndex)
                            Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(listState),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
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

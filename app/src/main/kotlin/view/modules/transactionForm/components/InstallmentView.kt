package view.modules.transactionForm.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.shared.TextMedium
import view.shared.TextSmall

@Composable
fun InstallmentView(
    installment: String
) {
    Row(modifier =
        Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextSmall(text = "Parcela:", modifier = Modifier.padding(end = 5.dp))
        TextMedium(
            modifier = Modifier.padding(end = 10.dp),
            text = installment,
            color = MaterialTheme.colors.secondary
        )
    }
}

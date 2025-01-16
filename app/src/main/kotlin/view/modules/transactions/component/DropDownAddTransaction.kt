package view.modules.transactions.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.fill.Circle
import view.shared.ClickableRow

@Composable
fun DropDownAddTransaction(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onClickGain: () -> Unit,
    onClickExpense: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        DropdownMenu(
            modifier = Modifier.padding(horizontal = 10.dp),
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {

            ClickableRow(
                icon = PhosphorIcons.Fill.Circle,
                iconColor = MaterialTheme.colors.onPrimary,
                label = "Nova receita"
            )
            { onClickGain() }

            ClickableRow(
                icon = PhosphorIcons.Fill.Circle,
                iconColor = MaterialTheme.colors.onError,
                label = "Nova despesa"
            )
            { onClickExpense() }

            // TO DO: Implements import tranasction
            // Divider()
            // ClickableRow(enabled = false, icon = PhosphorIcons.Light.Download, label = "Importar"){}
        }
    }
}

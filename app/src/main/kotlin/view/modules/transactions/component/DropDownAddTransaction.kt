package view.modules.transactions.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.fill.Circle
import view.shared.ClickableRow
import view.theme.Lime400
import view.theme.Red400

@Composable
fun DropDownAddTransaction(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onClickGain: () -> Unit,
    onClickExpense: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        DropdownMenu(
            modifier = Modifier.padding(horizontal = 10.dp),
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {

            ClickableRow(icon = PhosphorIcons.Fill.Circle, iconColor = Lime400, label = "Nova receita")
            { onClickGain() }

            ClickableRow(icon = PhosphorIcons.Fill.Circle, iconColor = Red400, label = "Nova despesa")
            { onClickExpense() }

            // TO DO: Implements import tranasction
            // Divider()
            // ClickableRow(enabled = false, icon = PhosphorIcons.Light.Download, label = "Importar"){}
        }
    }
}

package view.modules.transactions.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.fill.Circle
import core.entity.account.BankAccount
import core.enums.TransactionType
import view.modules.transactionForm.TransactionForm
import view.shared.ClickableRow
import view.theme.Lime400
import view.theme.Red400

@Composable
fun DropDownAddTransaction(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    selectedAccount: BankAccount
) {

    Box{
        DropdownMenu(
            modifier = Modifier.padding(horizontal = 10.dp),
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {

            var transactionType by remember { mutableStateOf(TransactionType.GAIN) }
            var showNewTransaction by remember { mutableStateOf(false) }

            ClickableRow(icon = PhosphorIcons.Fill.Circle, iconColor = Lime400, label = "Nova receita")
            { transactionType = TransactionType.GAIN; showNewTransaction = true;  }

            ClickableRow(icon = PhosphorIcons.Fill.Circle, iconColor = Red400, label = "Nova despesa")
            { transactionType = TransactionType.EXPENSE; showNewTransaction = true; }

            if (showNewTransaction)
                TransactionForm(
                    account = selectedAccount,
                    transactionType = transactionType,
                    onDismiss = onDismissRequest
                )


            // TODO: Implements import tranasction
            // Divider()
            // ClickableRow(enabled = false, icon = PhosphorIcons.Light.Download, label = "Importar"){}
        }

    }
}
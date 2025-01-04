package view.modules.transactions.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Pen
import com.adamglin.phosphoricons.light.Trash
import core.entity.Transaction
import view.modules.transactionForm.TransactionForm
import view.shared.ClickableRow
import view.shared.SimpleQuestionDialog
import viewModel.TransactionViewModel

@Composable
fun DropDownEditTransaction(
    viewModel: TransactionViewModel,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    selectedTransaction: Transaction,
) {

    Box{
        DropdownMenu(
            modifier = Modifier.padding(horizontal = 10.dp),
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {

            var showEditTransaction by remember { mutableStateOf(false) }
            ClickableRow(icon = PhosphorIcons.Light.Pen, label = "Editar") { showEditTransaction = true }
            if (showEditTransaction) {
                TransactionForm(
                    account = selectedTransaction.account,
                    transaction = selectedTransaction,
                    onDismiss = {
                        onDismissRequest()
                        showEditTransaction = false
                    }
                )
            }

            var showDeleteTransaction by remember { mutableStateOf(false) }
            ClickableRow(icon = PhosphorIcons.Light.Trash, label = "Excluir") { showDeleteTransaction = true }
            if (showDeleteTransaction) {
                SimpleQuestionDialog(
                    title = "GranaWiz",
                    message = "Tem certeza que seja excluir essa transação?",
                    onConfirmRequest = {
                        viewModel.deleteTransaction(selectedTransaction)
                        showDeleteTransaction = false
                        onDismissRequest()
                        viewModel.service.loadTransactions(viewModel.selectedAccount)
                    },
                    onDismissRequest = { showDeleteTransaction = false; onDismissRequest() },
                )
            }
        }

    }
}
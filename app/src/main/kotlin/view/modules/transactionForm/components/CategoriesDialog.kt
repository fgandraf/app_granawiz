package view.modules.transactionForm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import viewModel.TransactionFormViewModel


@Composable
fun CategoriesDialog(
    viewModel: TransactionFormViewModel,
    onDismissRequest: () -> Unit
) {

        Dialog(onDismissRequest = onDismissRequest ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(500.dp).defaultMinSize(minHeight = 500.dp)
                    .background(MaterialTheme.colors.background, shape = RoundedCornerShape(8.dp))
            ) {
                // IMPLEMENTS
            }
        }

}
package view.modules.transactionForm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.shared.TextPrimary
import viewModel.TransactionFormViewModel


@Composable
fun PartiesDialog(
    viewModel: TransactionFormViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.onPrimary, shape = RoundedCornerShape(8.dp))
    ) {
        TextPrimary(text = "PARTIES", weight = FontWeight.Bold, size = 22.sp)
    }
}
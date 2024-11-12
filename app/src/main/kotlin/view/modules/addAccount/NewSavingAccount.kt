package view.modules.addAccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import viewModel.AddAccountViewModel
import viewModel.SidebarViewModel

@Composable
fun NewSavingAccount(
    sidebarViewModel: SidebarViewModel,
    addAccountViewModel: AddAccountViewModel,
    onDismiss: () -> Unit
){

    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Conta Poupança")
    }

}
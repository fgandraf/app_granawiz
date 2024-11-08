package view.modules.addAccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import viewModel.AddAccountViewModel
import viewModel.SidebarViewModel

@Composable
fun NewCreditCard(
    sidebarViewModel: SidebarViewModel,
    addAccountViewModel: AddAccountViewModel
){

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Cartão de Crédito")
        Button(onClick = {
            sidebarViewModel.insertTestAccountOnGroup()
        }){
            Text(text = "Inserir conta teste")
        }
    }

}
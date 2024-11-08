 package view.modules.addAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import model.enums.AccountType
import view.shared.DialogTitleBar
import viewModel.SidebarViewModel

 @Composable
fun AddAccount(
     viewModel: SidebarViewModel,
     accountType: AccountType,
     onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(500.dp).height(400.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
        ) {

            //===== Title Bar
            val title = when (accountType) {
                AccountType.SAVINGS -> "Adicionar nova conta poupança"
                AccountType.CHECKING -> "Adicionar nova conta corrente"
                AccountType.CREDIT_CARD -> "Adicionar novo cartão de crédito"
            }
            DialogTitleBar(title, onDismiss)
            Divider()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 50.dp, bottom = 70.dp)
            ) {

                // Implements
                //ClickableRow(label = "Inserir conta de teste") { viewModel.insertTestAccountOnGroup() }

            }

        }
    }
}
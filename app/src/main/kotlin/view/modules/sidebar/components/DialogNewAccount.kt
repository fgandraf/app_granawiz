package view.modules.sidebar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import view.shared.ClickableRow
import viewModel.SidebarViewModel

@Composable
fun DialogNewAccount(
    viewModel: SidebarViewModel,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(600.dp)
                .height(400.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
                .padding(20.dp)
        ) {

            ClickableRow(
                label = "Inserir conta de teste",
                roundedBorder = true
            ){
                viewModel.insertTestAccountOnGroup()
            }
        }
    }
}
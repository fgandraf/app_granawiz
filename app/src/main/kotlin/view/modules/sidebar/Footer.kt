package view.modules.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import view.modules.sidebar.components.ButtonFooterItem
import view.modules.addAccount.AddAccount
import view.modules.sidebar.components.DialogNewGroup
import viewModel.SidebarViewModel

@Composable
fun Footer(viewModel: SidebarViewModel) {

    Divider()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .background(Color.White, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))

    ){



            var showNewGroupDialog by remember { mutableStateOf(false) }
            ButtonFooterItem(Modifier.weight(1f), "add_group.svg", "Novo grupo") { showNewGroupDialog = true }
            if (showNewGroupDialog)
                DialogNewGroup(
                    viewModel = viewModel,
                    onDismiss = { showNewGroupDialog = false }
                )

            Divider(modifier = Modifier.fillMaxHeight().width(1.dp))


            var showNewAccountDialog by remember { mutableStateOf(false) }
            ButtonFooterItem(Modifier.weight(1f), "add_account.svg", "Nova conta") {
                showNewAccountDialog = true
            }
            if (showNewAccountDialog)
                AddAccount(
                    viewModel = viewModel,
                    onDismiss = { showNewAccountDialog = false }
                )

    }

}
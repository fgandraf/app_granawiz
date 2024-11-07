package view.modules.sidebar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.utils.brMoney
import view.modules.settings.SettingsScreen
import view.shared.ClickableIcon
import view.shared.TextPrimary
import viewModel.SidebarViewModel

@Composable
fun Header(viewModel: SidebarViewModel) {

    Box(modifier = Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 20.dp)) {
        Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {


            var showDialog by remember { mutableStateOf(false) }
            ClickableIcon(icon = "settings", iconSize = 20.dp, shape = CircleShape) { showDialog = true }
            if (showDialog)
                SettingsScreen(onDismiss = { showDialog = false })



        }
        Column(modifier = Modifier.align(Alignment.Center)) {
            TextPrimary(text = brMoney.format(viewModel.totalAccounts), size = 16.sp, weight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(bottom = 3.dp), align = TextAlign.Center)
            TextPrimary(text = "SALDO TOTAL", size = 8.sp, modifier = Modifier.fillMaxWidth(), align = TextAlign.Center)
        }
    }
    Divider()
}
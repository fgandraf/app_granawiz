package view.modules.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Gear
import utils.brMoney
import view.modules.settings.SettingsScreen
import view.shared.ClickableIcon
import view.shared.TextH4
import view.shared.TextH2
import viewModel.SidebarViewModel

@Composable
fun Header(viewModel: SidebarViewModel) {

    Box(modifier = Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 20.dp)) {

        Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            var showDialog by remember { mutableStateOf(false) }
            ClickableIcon(icon = PhosphorIcons.Regular.Gear, iconSize = 22.dp, shape = CircleShape) {
                showDialog = true
            }
            if (showDialog)
                SettingsScreen(onDismiss = { showDialog = false })
        }

        Column(modifier = Modifier.align(Alignment.Center)) {
            TextH2(
                text = brMoney.format(viewModel.total.value),
                modifier = Modifier.fillMaxWidth().padding(bottom = 3.dp),
                align = TextAlign.Center
            )
            TextH4(text = "SALDO TOTAL", modifier = Modifier.fillMaxWidth(), align = TextAlign.Center)
        }
    }
    Divider(Modifier.height(0.5.dp).background(MaterialTheme.colors.onSurface))
}
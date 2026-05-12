package view.modules.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Gear
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import utils.formatCurrency
import viewModel.UserPreferences
import view.modules.settings.SettingsScreen
import view.shared.ClickableIcon
import view.shared.TextH4
import view.shared.TextH2
import viewModel.SidebarViewModel

@Composable
fun Header(
    viewModel: SidebarViewModel,
    showSettings: Boolean = false,
    onSettingsChange: (Boolean) -> Unit = {},
) {

    Box(modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 20.dp)) {

        Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            ClickableIcon(icon = PhosphorIcons.Regular.Gear, iconSize = 22.dp, shape = CircleShape) {
                onSettingsChange(true)
            }
            if (showSettings)
                SettingsScreen(onDismiss = { onSettingsChange(false) })
        }

        val total by viewModel.total.collectAsState()
        Column(modifier = Modifier.align(Alignment.Center)) {
            TextH2(
                text = formatCurrency(total, UserPreferences.currencySymbol),
                modifier = Modifier.fillMaxWidth().padding(bottom = 3.dp),
                align = TextAlign.Center
            )
            TextH4(text = stringResource(Res.string.sidebar_total_balance), modifier = Modifier.fillMaxWidth(), align = TextAlign.Center)
        }
    }
    Divider(Modifier.height(0.5.dp).background(MaterialTheme.colors.onSurface))
}
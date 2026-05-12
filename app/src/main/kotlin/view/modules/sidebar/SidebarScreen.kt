package view.modules.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.modules.Screen
import viewModel.SidebarViewModel

@Composable
fun Sidebar(
    viewModel: SidebarViewModel = SidebarViewModel(),
    currentScreen: Screen,
    showSettings: Boolean = false,
    onSettingsChange: (Boolean) -> Unit = {},
    onScreenSelected: (Screen) -> Unit,
) {

    Row {

        Column(modifier = Modifier.width(229.5.dp).fillMaxHeight().background(MaterialTheme.colors.background)) {
            Header(viewModel, showSettings = showSettings, onSettingsChange = onSettingsChange)
            Main(Modifier.weight(1f), viewModel, currentScreen, onScreenSelected)
            Footer(viewModel)
        }

        Divider(Modifier.fillMaxHeight().width(0.5.dp).background(MaterialTheme.colors.onSurface))

    }

}
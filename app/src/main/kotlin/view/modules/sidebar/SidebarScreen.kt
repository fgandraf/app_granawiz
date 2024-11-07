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
import view.theme.Gray200
import viewModel.SidebarViewModel

@Composable
fun Sidebar(
    viewModel: SidebarViewModel = SidebarViewModel(),
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {

    Row {

        Column(modifier = Modifier.width(259.5.dp).fillMaxHeight().background(Gray200)) {
            Header(viewModel)
            Main(Modifier.weight(1f), viewModel, currentScreen, onScreenSelected)
            Footer(viewModel)
        }

        Divider(Modifier.fillMaxHeight().width(0.5.dp).background(MaterialTheme.colors.primaryVariant))

    }

}
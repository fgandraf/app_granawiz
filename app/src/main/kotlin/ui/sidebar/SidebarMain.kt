package ui.sidebar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.sidebar.components.MenuItem

@Composable
fun SidebarMain(modifier: Modifier = Modifier) {

    Column(modifier = modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.height(25.dp))
        MenuItem(iconResource = "icons/dashboard.svg", label = "Dashboard") { }

        MenuItem(iconResource = "icons/category.svg", label = "Categorias", submenu = true) { }
        MenuItem(iconResource = "icons/tag.svg", label = "Etiquetas", submenu = true) { }
        MenuItem(iconResource = "icons/recipient.svg", label = "Recebedores", submenu = true) { }
        MenuItem(iconResource = "icons/payer.svg", label = "Pagadores", submenu = true) { }

        Spacer(modifier = Modifier.height(10.dp))
        MenuItem(iconResource = "icons/list.svg", label = "Todas as transações") { }

    }
}
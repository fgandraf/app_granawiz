package view.modules.sidebar

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.*
import view.modules.Screen
import view.modules.sidebar.components.AccountMenuItem
import view.modules.sidebar.components.GroupMenuItem
import view.modules.sidebar.components.SectionTitle
import view.modules.sidebar.components.StaticMenuItem
import view.shared.TextPrimary
import viewModel.SidebarViewModel

@Composable
fun Main(
    modifier: Modifier = Modifier,
    viewModel: SidebarViewModel,
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    ){


    Column(modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {

        //=== STATIC MENU ITEMS
        //Spacer(modifier = Modifier.height(25.dp))
//        StaticMenuItem(
//            icon = PhosphorIcons.Light.SquaresFour,
//            label = "Dashboard",
//            screen = Screen.Dashboard,
//            currentScreen = currentScreen,
//            onClick = onScreenSelected
//        )
//        StaticMenuItem(
//            icon = PhosphorIcons.Light.Calendar,
//            label = "Agendamentos",
//            screen = Screen.Schedules,
//            currentScreen = currentScreen,
//            onClick = onScreenSelected
//        )
//        StaticMenuItem(
//            icon = PhosphorIcons.Light.Scroll,
//            label = "Relatórios",
//            screen = Screen.Reports,
//            currentScreen = currentScreen,
//            onClick = onScreenSelected
//        )
        SectionTitle("Base de dados")
        StaticMenuItem(
            icon = PhosphorIcons.Light.Shapes,
            label = "Categorias",
            screen = Screen.Categories,
            currentScreen = currentScreen,
            onClick = onScreenSelected
        )
        StaticMenuItem(
            icon = PhosphorIcons.Light.Tag,
            label = "Etiquetas",
            screen = Screen.Tags,
            currentScreen = currentScreen,
            onClick = onScreenSelected
        )
        StaticMenuItem(
            icon = PhosphorIcons.Light.HandArrowDown,
            label = "Beneficiários",
            screen = Screen.Receivers,
            currentScreen = currentScreen,
            onClick = onScreenSelected
        )
        StaticMenuItem(
            icon = PhosphorIcons.Light.HandArrowUp,
            label = "Pagadores",
            screen = Screen.Payers,
            currentScreen = currentScreen,
            onClick = onScreenSelected
        )
        SectionTitle("Transações")
        StaticMenuItem(
            icon = PhosphorIcons.Light.ListBullets,
            label = "Todas as transações",
            screen = Screen.Transactions(),
            currentScreen = currentScreen,
            onClick = onScreenSelected
        )
        Spacer(Modifier.height(10.dp))


        //=== DYNAMIC MENU ITEMS
        if (viewModel.groups.value.isEmpty())
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextPrimary(text = "Nenhum grupo criado", italic = true, size = 10.sp)
            }
        else {
            val expandedGroups = remember { mutableStateMapOf<String, Boolean>() }
            viewModel.groups.value.forEach { group ->

                val isExpanded = expandedGroups[group.name] ?: true
                GroupMenuItem(
                    viewModel = viewModel,
                    group = group,
                    isExpanded = isExpanded,
                    toggleClick = { expandedGroups[group.name] = !isExpanded }
                )

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        group.accounts.forEach { account ->
                            AccountMenuItem(
                                viewModel = viewModel,
                                account = account,
                                screen = Screen.Transactions(account, showAddButton = true),
                                currentScreen = currentScreen,
                                onClick = onScreenSelected
                            )
                        }
                    }
                }
                Spacer(Modifier.height(5.dp))
            }
        }

    }

}
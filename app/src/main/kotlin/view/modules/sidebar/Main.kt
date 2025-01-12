package view.modules.sidebar

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
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

    var activeAccountId by remember { mutableStateOf(0L) }

    Column(modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {

        //=== STATIC MENU ITEMS


        // TODO: Implements Dashboard
//        Spacer(modifier = Modifier.height(25.dp))
//        StaticMenuItem(
//            icon = PhosphorIcons.Light.SquaresFour,
//            label = "Dashboard",
//            isActive = currentScreen == Screen.Dashboard,
//            screen = Screen.Dashboard,
//            onClick = onScreenSelected
//        )


        // TODO: Implements Schedules
//        StaticMenuItem(
//            icon = PhosphorIcons.Light.Calendar,
//            label = "Agendamentos",
//            isActive = currentScreen == Schedules.Dashboard,
//            screen = Screen.Schedules,
//            onClick = onScreenSelected
//        )


        // TODO: Implements Reports
//        StaticMenuItem(
//            icon = PhosphorIcons.Light.Scroll,
//            label = "Relatórios",
//            isActive = currentScreen == Schedules.Reports,
//            screen = Screen.Reports,
//            onClick = onScreenSelected
//        )


        SectionTitle("Base de dados")
        StaticMenuItem(
            icon = PhosphorIcons.Light.Shapes,
            label = "Categorias",
            isActive = currentScreen == Screen.Categories,
            screen = Screen.Categories,
            onClick = { onScreenSelected(it); activeAccountId = 0L }
        )
        StaticMenuItem(
            icon = PhosphorIcons.Light.Tag,
            label = "Etiquetas",
            isActive = currentScreen == Screen.Tags,
            screen = Screen.Tags,
            onClick = { onScreenSelected(it); activeAccountId = 0L }
        )
        StaticMenuItem(
            icon = PhosphorIcons.Light.HandArrowUp,
            label = "Beneficiários",
            isActive = currentScreen == Screen.Receivers,
            screen = Screen.Receivers,
            onClick = { onScreenSelected(it); activeAccountId = 0L }
        )
        StaticMenuItem(
            icon = PhosphorIcons.Light.HandArrowDown,
            label = "Pagadores",
            isActive = currentScreen == Screen.Payers,
            screen = Screen.Payers,
            onClick = { onScreenSelected(it); activeAccountId = 0L }
        )
        SectionTitle("Transações")
        StaticMenuItem(
            icon = PhosphorIcons.Light.ListBullets,
            label = "Todas as transações",
            isActive = currentScreen == Screen.Transactions(),
            screen = Screen.Transactions(),
            onClick = { onScreenSelected(it); activeAccountId = 0L }
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
                                isActive = activeAccountId == account.id,
                                screen = Screen.Transactions(account, showAddButton = true),
                                onClick =  {onScreenSelected(it); activeAccountId = account.id }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(5.dp))
            }
        }

    }

}
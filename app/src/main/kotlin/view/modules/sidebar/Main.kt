package view.modules.sidebar

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.*
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import view.modules.Screen
import view.modules.sidebar.components.AccountMenuItem
import view.modules.sidebar.components.GroupMenuItem
import view.modules.sidebar.components.SectionTitle
import view.modules.sidebar.components.StaticMenuItem
import view.shared.TextSmall
import viewModel.SidebarViewModel

@Composable
fun Main(
    modifier: Modifier = Modifier,
    viewModel: SidebarViewModel,
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
) {

    var activeAccountId by remember { mutableStateOf(0L) }

    Column(modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {

        //=== STATIC MENU ITEMS
        Spacer(modifier = Modifier.height(25.dp))
        StaticMenuItem(
            icon = PhosphorIcons.Light.SquaresFour,
            label = stringResource(Res.string.nav_dashboard),
            isActive = currentScreen == Screen.Dashboard,
            screen = Screen.Dashboard,
            onClick = { onScreenSelected(it); activeAccountId = 0L }
        )


        StaticMenuItem(
            icon = PhosphorIcons.Light.Calendar,
            label = stringResource(Res.string.nav_schedules),
            isActive = currentScreen == Screen.Schedules,
            screen = Screen.Schedules,
            onClick = { onScreenSelected(it); activeAccountId = 0L }
        )


        SectionTitle(stringResource(Res.string.sidebar_database_section))
        StaticMenuItem(
            icon = PhosphorIcons.Light.Shapes,
            label = stringResource(Res.string.nav_categories),
            isActive = currentScreen == Screen.Categories,
            screen = Screen.Categories,
            onClick = { onScreenSelected(it); activeAccountId = 0L }
        )
        StaticMenuItem(
            icon = PhosphorIcons.Light.Tag,
            label = stringResource(Res.string.nav_tags),
            isActive = currentScreen == Screen.Tags,
            screen = Screen.Tags,
            onClick = { onScreenSelected(it); activeAccountId = 0L }
        )
        StaticMenuItem(
            icon = PhosphorIcons.Light.HandArrowUp,
            label = stringResource(Res.string.nav_receivers),
            isActive = currentScreen == Screen.Receivers,
            screen = Screen.Receivers,
            onClick = { onScreenSelected(it); activeAccountId = 0L }
        )
        StaticMenuItem(
            icon = PhosphorIcons.Light.HandArrowDown,
            label = stringResource(Res.string.nav_payers),
            isActive = currentScreen == Screen.Payers,
            screen = Screen.Payers,
            onClick = { onScreenSelected(it); activeAccountId = 0L }
        )
        SectionTitle(stringResource(Res.string.sidebar_transactions_section))
        StaticMenuItem(
            icon = PhosphorIcons.Light.ListBullets,
            label = stringResource(Res.string.nav_all_transactions),
            isActive = currentScreen == Screen.Transactions(),
            screen = Screen.Transactions(),
            onClick = { onScreenSelected(it); activeAccountId = 0L }
        )
        Spacer(Modifier.height(10.dp))


        val groups by viewModel.groups.collectAsState()
        //=== DYNAMIC MENU ITEMS
        if (groups.isEmpty())
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextSmall(text = stringResource(Res.string.sidebar_no_groups), italic = true)
            }
        else {
            val expandedGroups = remember { mutableStateMapOf<String, Boolean>() }
            groups.forEach { group ->

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
                                onClick = { onScreenSelected(it); activeAccountId = account.id }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(5.dp))
            }
        }

    }

}
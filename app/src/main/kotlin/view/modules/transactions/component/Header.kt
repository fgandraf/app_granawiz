package view.modules.transactions.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.felipegandra.generated.resources.Res
import domain.entity.account.BankAccount
import domain.structs.PageAddress
import org.jetbrains.compose.resources.stringResource
import view.shared.AddressView
import view.shared.ClickableIcon
import view.shared.SearchField
import view.shared.TextNormal

@Composable
fun Header(
    backIcon: Boolean,
    addresses: List<PageAddress>,
    initialAddress: List<PageAddress>,
    showTransactionsList: Boolean,
    transactionsState: List<*>,
    searchQuery: String,
    account: BankAccount?,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 20.dp, end = 20.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(30.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            // address row
            Row(verticalAlignment = Alignment.CenterVertically) {
                ClickableIcon(
                    enabled = backIcon,
                    icon = PhosphorIcons.Bold.ArrowLeft,
                    iconSize = 22.dp,
                    boxSize = 25.dp
                ) {
                    onBackClick()
                }
                Spacer(Modifier.width(10.dp))
                addresses.forEach {
                    AddressView(
                        icon = it.iconVector,
                        iconSize = it.iconSize!!,
                        value = it.name,
                        rootPath = it.rootPath
                    )
                }
            }

            if (showTransactionsList && transactionsState.isNotEmpty())
                SearchField(
                    value = searchQuery,
                    onValueChange = { onSearchQueryChange(it) }
                )
        }

        if (account?.description?.isNotEmpty() == true) {
            TextNormal(
                text = account.description,
                align = TextAlign.Start,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

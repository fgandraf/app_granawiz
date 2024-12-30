package view.modules.transactions

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Folders
import com.adamglin.phosphoricons.light.ListBullets
import com.adamglin.phosphoricons.light.Plus
import core.entity.Transaction
import core.entity.account.BankAccount
import utils.IconPaths
import view.modules.transactionForm.TransactionForm
import view.modules.transactions.component.MonthHeader
import view.modules.transactions.component.TransactionRow
import view.shared.AddressView
import view.shared.SearchBar
import view.shared.TextPrimary
import view.theme.Purple600
import viewModel.TransactionViewModel

@Composable
fun TransactionsScreen(
    account: BankAccount? = null,
    showAddButton: Boolean = true,
    viewModel: TransactionViewModel = TransactionViewModel(account),
) {

    var selectedTransaction by remember { mutableStateOf(Transaction()) }
    var showAddOrEditTransaction by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {


        // HEADER
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ){
            //Address
            Column{
                if (account != null) {
                    Row(Modifier.padding(bottom = 12.dp)) {
                        AddressView(icon = PhosphorIcons.Light.Folders, value = account.group.name)
                        AddressView(IconPaths.BANK_LOGOS + account.icon, value = account.name, iconSize = DpSize(20.dp, 16.dp))
                    }
                    TextPrimary(
                        text = account.description,
                        size = 12.sp,
                        align = TextAlign.Start
                    )
                } else{
                    AddressView(icon = PhosphorIcons.Light.ListBullets, value = "Todas as transações")
                }
            }

            //Searchbar
            SearchBar(onTuneClicked = {}, onSearchClicked = {})
        }



        // BODY
        Box(modifier = Modifier.fillMaxSize()) {
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {

                val groupedTransactions = viewModel.transactions.value.groupBy { it.date.month }
                groupedTransactions.forEach { (month, transactions) ->

                    item {
                        var negativeBalnce = 0.0
                        var positiveBalance = 0.0
                        transactions.forEach { transaction ->
                            if (transaction.balance >= 0) positiveBalance += transaction.balance
                            else negativeBalnce -= transaction.balance
                        }
                        MonthHeader(month, incomeBalance = positiveBalance, outcomeBalance = negativeBalnce)

                        Column(modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colors.onPrimary, RoundedCornerShape(10.dp))
                            .border(0.5.dp, MaterialTheme.colors.primaryVariant.copy(0.8f), RoundedCornerShape(10.dp))
                        ) {
                            Spacer(Modifier.height(10.dp))

                            var count = transactions.count()
                            transactions.forEach { transaction ->
                                TransactionRow(
                                    transaction = transaction,
                                    onClick = {
                                        selectedTransaction = transaction
                                        showAddOrEditTransaction = true
                                    }
                                )
                                count--
                                if (count > 0) Divider(modifier = Modifier.padding(horizontal = 20.dp), color = MaterialTheme.colors.primaryVariant.copy(0.4f))
                            }
                            Spacer(Modifier.height(10.dp))
                        }
                    }

                }

                item { Spacer(Modifier.height(50.dp)) }
            }
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(listState),
                modifier = Modifier.align(Alignment.CenterEnd)
            )


            if (showAddButton){
                Box(modifier = Modifier.fillMaxSize().padding(bottom = 50.dp, end = 50.dp)) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Purple600.copy(alpha = 0.8f)  )
                            .align(Alignment.BottomEnd)
                            .pointerHoverIcon(PointerIcon.Hand)
                            .clickable { showAddOrEditTransaction = true }
                    ){
                        Icon(imageVector = PhosphorIcons.Light.Plus, contentDescription = "Add transaction", tint = Color.White, modifier = Modifier
                            .size(25.dp).align(Alignment.Center))
                    }
                }

            }
            if (showAddOrEditTransaction) {
                if (viewModel.selectedAccount == null) viewModel.selectAccount(selectedTransaction.account)
                TransactionForm(
                    account = viewModel.selectedAccount!!,
                    transaction = selectedTransaction,
                    onDismiss = {
                        selectedTransaction = Transaction()
                        showAddOrEditTransaction = false
                    }
                )
            }

        }
    }
}
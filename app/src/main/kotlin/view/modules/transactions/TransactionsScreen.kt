package view.modules.transactions

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.zIndex
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Folders
import com.adamglin.phosphoricons.light.ListBullets
import com.adamglin.phosphoricons.light.Plus
import core.entity.Transaction
import core.entity.account.BankAccount
import utils.IconPaths
import view.modules.transactionForm.TransactionForm
import view.modules.transactions.component.DropDownAddTransaction
import view.modules.transactions.component.MonthHeader
import view.modules.transactions.component.TotalFooter
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
    var showEditTransaction by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            //.background(Brush.horizontalGradient(listOf(MaterialTheme.colors.background, Color.White)))
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
                modifier = Modifier.fillMaxSize(),
            ) {
                item { Spacer(modifier = Modifier.height(30.dp)) }

                val groupedTransactions = viewModel.transactions.value.groupBy { it.date.month }
                groupedTransactions.forEach { (month, transactions) ->

                    item {

                        MonthHeader(month = month)

                        var negativeBalnce = 0.0
                        var positiveBalance = 0.0
                        val corners = RoundedCornerShape(topEnd = 20.dp, bottomStart = 20.dp)

                        Column(modifier = Modifier
                            .zIndex(1f)
                            .padding(horizontal = 90.dp)
                            .clip(corners)
                            .background(MaterialTheme.colors.onPrimary, corners)
                            .border(0.5.dp, MaterialTheme.colors.primary, corners)
                        ) {
                            Spacer(Modifier.height(20.dp))

                            var count = transactions.count()
                            //Divider(modifier = Modifier.padding(horizontal = 30.dp), color = MaterialTheme.colors.primaryVariant.copy(0.4f))
                            transactions.forEach { transaction ->
                                if (transaction.balance >= 0) positiveBalance += transaction.balance
                                else negativeBalnce -= transaction.balance

                                TransactionRow(
                                    viewModel = viewModel,
                                    transaction = transaction,
                                    onClick = {
                                        selectedTransaction = transaction
                                        showEditTransaction = true
                                    }
                                )
                                count--
                                //if (count > 0) Divider(modifier = Modifier.padding(horizontal = 30.dp), color = MaterialTheme.colors.primaryVariant.copy(0.4f))
                                //Divider(modifier = Modifier.padding(horizontal = 30.dp), color = MaterialTheme.colors.primaryVariant.copy(0.4f))
                            }
                            Spacer(Modifier.height(20.dp))
                        }

                        TotalFooter(incomeBalance = positiveBalance, outcomeBalance = negativeBalnce)
                        Spacer(Modifier.height(30.dp))
                    }
                }

                item { Spacer(Modifier.height(50.dp)) }
            }
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(listState),
                modifier = Modifier.align(Alignment.CenterEnd)
            )


            if (showAddButton){
                var showNewTransaction by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxSize().padding(bottom = 50.dp, end = 50.dp)) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Purple600.copy(alpha = 0.8f)  )
                            .align(Alignment.BottomEnd)
                            .pointerHoverIcon(PointerIcon.Hand)
                            .clickable { showNewTransaction = true }
                    ){
                        Icon(
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.Center),
                            imageVector = PhosphorIcons.Light.Plus,
                            contentDescription = "Add transaction",
                            tint = Color.White
                        )

                        if (showNewTransaction) {
                            DropDownAddTransaction(
                                expanded = showNewTransaction,
                                selectedAccount = viewModel.selectedAccount!!,
                                onDismissRequest = {
                                    selectedTransaction = Transaction()
                                    showNewTransaction = false
                                }
                            )
                        }
                    }
                }
            }

            if (showEditTransaction) {
                if (viewModel.selectedAccount == null) viewModel.selectAccount(selectedTransaction.account)
                TransactionForm(
                    account = viewModel.selectedAccount!!,
                    transaction = selectedTransaction,
                    onDismiss = {
                        selectedTransaction = Transaction()
                        showEditTransaction = false
                    }
                )
            }

        }
    }
}
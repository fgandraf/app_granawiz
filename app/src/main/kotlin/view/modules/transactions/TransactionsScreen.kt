package view.modules.transactions

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.IconPaths
import model.entity.account.BankAccount
import view.modules.transactions.component.TransactionRow
import view.shared.AddressView
import view.shared.SearchBar
import view.shared.TextPrimary
import viewModel.TransactionViewModel

@Composable
fun TransactionsScreen(
    account: BankAccount?,
    viewModel: TransactionViewModel = TransactionViewModel(account),
) {

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
                        AddressView(IconPaths.SYSTEM_ICONS + "group.svg", account.group.name)
                        AddressView(IconPaths.BANK_LOGOS + account.icon, account.name, DpSize(20.dp, 16.dp))
                    }
                    TextPrimary(
                        text = account.description,
                        size = 12.sp,
                        align = TextAlign.Start
                    )
                } else{
                    AddressView(IconPaths.SYSTEM_ICONS + "list.svg", "Todas as transações")
                }
            }

            //Searchbar
            SearchBar(onTuneClicked = {}, onSearchClicked = {})
        }




        // BODY
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 80.dp)
                .height(600.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
        ) {
            Spacer(Modifier.height(50.dp))

            Box{


                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {


                    items(viewModel.transactions, key = { it.id }) { transaction ->
                        TransactionRow(transaction) {}
                    }


                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(listState),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }




        }


    }
}
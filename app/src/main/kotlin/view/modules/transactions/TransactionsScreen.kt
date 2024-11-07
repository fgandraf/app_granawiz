package view.modules.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.IconPaths
import model.entity.account.BankAccount
import view.shared.AddressView
import view.shared.SearchBar
import view.shared.TextPrimary

@Composable
fun TransactionsScreen(account: BankAccount?) {

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
        // Implements


    }
}
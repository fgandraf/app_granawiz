package view.modules.transactions.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.brMoney
import view.shared.TextPrimary
import view.theme.Afacade
import view.theme.Lime200
import view.theme.Red200
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.*

@Composable
fun MonthHeader(
    month: Month,
    incomeBalance: Double,
    outcomeBalance: Double
){

    var monthTitle = "Esse mês"
    if (month != LocalDate.now().month)
        monthTitle = month.getDisplayName(TextStyle.FULL, Locale.of("pt", "br")).replaceFirstChar { it.uppercase() }

    Column{

        Spacer(modifier = Modifier.height(30.dp))
        TextPrimary(
            modifier = Modifier.padding(start = 30.dp),
            text = monthTitle,
            fontFamily = Afacade,
            size = 22.sp
        )


        Row(modifier = Modifier.padding(start = 30.dp), verticalAlignment = Alignment.CenterVertically) {

            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(2.dp)
            ) {
                TextPrimary(text = brMoney.format(incomeBalance), color = Lime200, size = 11.sp, weight = FontWeight.Normal)
            }


            TextPrimary(text = " - ", size = 11.sp, weight = FontWeight.Normal)


            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(2.dp)
            ) {
                TextPrimary(text = brMoney.format(outcomeBalance), color = Red200, size = 11.sp, weight = FontWeight.Normal)
            }


            TextPrimary(text = " = ", size = 11.sp, weight = FontWeight.Normal)


            Row(horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(2.dp)
            ) {
                val total = incomeBalance - outcomeBalance
                TextPrimary(text = brMoney.format(total), color = if (total >= 0.0) Lime200 else Red200, size = 11.sp, weight = FontWeight.Normal)
            }

        }

        Spacer(modifier = Modifier.height(10.dp))
    }

}
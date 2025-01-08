package view.modules.transactions.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    Column(modifier = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
        .background(MaterialTheme.colors.onPrimary)//, RoundedCornerShape(4.dp))
        //.border(0.5.dp, MaterialTheme.colors.primary)//, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
    ){

//        Spacer(modifier = Modifier.height(30.dp))
        Spacer(modifier = Modifier.height(5.dp))


        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {

            TextPrimary(
                text = monthTitle,
                fontFamily = Afacade,
                size = 22.sp
            )
            Row {
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



        }

        Spacer(modifier = Modifier.height(5.dp))
    }

}
package view.modules.transactions.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.utils.brMoney
import view.shared.TextPrimary
import view.theme.Lime700
import view.theme.Red400
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

    Column(modifier = Modifier.padding(30.dp)) {
        TextPrimary(
            modifier = Modifier.padding(bottom = 10.dp),
            text = monthTitle,
            color = Color.Black,
            weight = FontWeight.Medium,
            size = 18.sp
        )


        Row {

            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, Lime700, RoundedCornerShape(4.dp))
                    .padding(2.dp)
            ) {
                TextPrimary(modifier = Modifier.padding(horizontal = 20.dp), text = brMoney.format(incomeBalance), color = Lime700, size = 12.sp, weight = FontWeight.Medium)
            }

            TextPrimary(text = " - ", size = 12.sp, weight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 5.dp))

            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, Red400, RoundedCornerShape(4.dp))
                    .padding(2.dp)
            ) {
                TextPrimary(modifier = Modifier.padding(horizontal = 20.dp), text = brMoney.format(outcomeBalance), color = Red400, size = 12.sp, weight = FontWeight.Medium)
            }

            TextPrimary(text = " = ", size = 12.sp, weight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 5.dp))

            Row(horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .padding(2.dp)
            ) {
                TextPrimary(modifier = Modifier.padding(horizontal = 20.dp), text = brMoney.format(incomeBalance - outcomeBalance), color = Color.Gray, size = 12.sp, weight = FontWeight.Medium)
            }

        }
    }

}
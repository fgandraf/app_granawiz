package view.modules.transactions.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import utils.brMoney
import view.shared.TextPrimary
import view.theme.Lime200
import view.theme.Red200

@Composable
fun TotalFooter(
    incomeBalance: Double,
    outcomeBalance: Double,
){

    val corners = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)
    var boxWidth by remember { mutableStateOf(0) }


    Box(Modifier.fillMaxWidth().offset(y = (-1).dp)
        //.zIndex(30f)
        .padding(end = 90.dp)) {


        Box(Modifier
            .align(Alignment.BottomEnd)
            .clip(corners)
            .background(MaterialTheme.colors.background, corners)
            .border(1.dp, MaterialTheme.colors.primaryVariant, corners)
            .onGloballyPositioned { boxWidth = it.size.width }
            .zIndex(1f)
        ) {
            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                TextPrimary(
                    text = brMoney.format(incomeBalance),
                    color = Lime200,
                    size = 11.sp,
                    weight = FontWeight.Normal
                )
                TextPrimary(text = " - ", size = 11.sp, modifier = Modifier.padding(horizontal = 5.dp))
                TextPrimary(
                    text = brMoney.format(outcomeBalance),
                    color = Red200,
                    size = 11.sp,
                    weight = FontWeight.Normal
                )
                TextPrimary(text = " = ", size = 11.sp, modifier = Modifier.padding(horizontal = 5.dp))
                val total: Double = incomeBalance - outcomeBalance
                TextPrimary(
                    text = brMoney.format(total),
                    color = if (total >= 0.0) Lime200 else Red200,
                    size = 12.sp,
                    weight = FontWeight.Normal
                )
            }
        }


//
//        Box(modifier = Modifier
//            .padding(end = 0.5.dp)
//            .width(with(LocalDensity.current) { boxWidth.toDp() - 1.dp })
//            .height(2.dp)
//            .background(Color.White)
//            .align(Alignment.TopEnd)
//            .zIndex(3f)
//        ) { }
//
//
//
//        Box(modifier = Modifier
//            .padding(end = 8.dp)
//            .width(with(LocalDensity.current) { boxWidth.toDp() - 16.dp })
//            .height(0.5.dp)
//            .background(MaterialTheme.colors.primaryVariant)
//            .align(Alignment.TopEnd)
//            .zIndex(3f)
//        ) { }





    }



}
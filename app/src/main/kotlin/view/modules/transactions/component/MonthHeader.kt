package view.modules.transactions.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import view.shared.TextPrimary
import view.theme.Afacade
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.*

@Composable
fun MonthHeader(
    month: Month
){
    val formatedMonth = month.getDisplayName(TextStyle.FULL, Locale.of("pt", "br")).replaceFirstChar { it.uppercase() }
    val monthTitle = if (month != LocalDate.now().month) formatedMonth else "Esse mês"
    val corners = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)

    var boxWidth by remember { mutableStateOf(0) }

    Box(Modifier
        .offset(y = 1.dp)
        //.zIndex(30f)
        .padding(start = 90.dp)
    ) {
        Box(Modifier
            .clip(corners)
            .background(MaterialTheme.colors.background, corners)
            .border(1.dp, MaterialTheme.colors.primaryVariant, corners)
            .onGloballyPositioned { boxWidth = it.size.width }
            .zIndex(1f)
        ) {
            TextPrimary(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                text = monthTitle,
                fontFamily = Afacade,
                weight = FontWeight.Bold,
                size = 26.sp,
            )
        }

//        Box(modifier = Modifier
//            .padding(start = 0.5.dp)
//            .width(with(LocalDensity.current) { boxWidth.toDp() - 1.dp })
//            .height(2.dp)
//            .background(Color.White)
//            .align(Alignment.BottomStart)
//            .zIndex(3f)
//        ) { }
//
//        Box(modifier = Modifier
//            .padding(start = 8.dp)
//            .width(with(LocalDensity.current) { boxWidth.toDp() - 16.dp })
//            .height(0.5.dp)
//            .background(MaterialTheme.colors.primaryVariant)
//            .align(Alignment.BottomStart)
//            .zIndex(3f)
//        ) { }

    }


}
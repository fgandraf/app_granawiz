package view.modules.transactions.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.DotsThree
import com.adamglin.phosphoricons.light.Tag
import core.entity.Transaction
import core.enums.TransactionType
import utils.IconPaths
import utils.brMoney
import view.shared.ClickableIcon
import view.shared.TextPrimary
import view.theme.Gray400
import view.theme.Lime400
import view.theme.Red400
import java.time.format.TextStyle
import java.util.*

@Composable
fun TransactionRow(
    transaction: Transaction,
    onClick: () -> Unit
){

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(45.dp)
                .clip(RoundedCornerShape(6.dp))
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { onClick() }
        ) {


            // Transaction Type
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight().padding(start = 20.dp, end = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (transaction.type == TransactionType.GAIN) Lime400 else if (transaction.type == TransactionType.EXPENSE) Red400 else Gray400)
                        .size(10.dp)
                )
            }


            // Party Name and Date
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.8f)
                    .padding(end = 20.dp)
            ) {
                TextPrimary(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = transaction.party.name,
                    weight = FontWeight.Medium
                )
                val day = transaction.date.dayOfMonth
                val month = transaction.date.month.getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"))
                TextPrimary(text = "$day $month", size = 10.sp)
            }


            // Category and Subcategory
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.9f)
                    .padding(end = 20.dp)
            ) {

                Icon(
                    painter = painterResource(IconPaths.CATEGORY_PACK + transaction.category.icon),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(15.dp)
                )

                Spacer(Modifier.width(10.dp))
                TextPrimary(text = transaction.category.name, size = 11.sp)

                if (transaction.subcategory != null) {
                    Spacer(Modifier.width(5.dp))
                    TextPrimary(text = "→")
                    Spacer(Modifier.width(5.dp))
                    TextPrimary(text = transaction.subcategory!!.name, size = 11.sp)
                }
            }

            // Tags
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.9f)
            ) {
                if (transaction.tags?.isNotEmpty()!!) {
                    transaction.tags.forEach { tag ->
                        Row {
                            Icon(
                                imageVector = PhosphorIcons.Light.Tag,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(Modifier.width(5.dp))
                            TextPrimary(text = tag.name, size = 10.sp)
                            Spacer(Modifier.width(15.dp))
                        }
                    }
                }

            }

            // Balance
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
                    //.weight(0.3f)
                    .padding(end = 20.dp)
            ) {
                TextPrimary(text = brMoney.format(transaction.balance), size = 11.sp)
            }




            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 10.dp)
            ) {
                ClickableIcon(
                    icon = PhosphorIcons.Light.DotsThree,
                    shape = RoundedCornerShape(6.dp),
                    onClick = { }
                )

            }



        }


    }

}
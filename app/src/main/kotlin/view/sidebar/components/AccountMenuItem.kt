package view.sidebar.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import utils.brMoney
import ui.theme.Afacade
import ui.theme.Lime700
import ui.theme.Red400
import ui.theme.Ubuntu

@Composable
fun AccountMenuItem(
    iconResource: String,
    label: String,
    value: Float,
    onClick: () -> Unit
) {

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .height(40.dp)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { onClick() }
    ) {

        Icon(
            painter = painterResource(iconResource),
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(25.dp).offset(x = 25.dp)
        )


        Column(modifier = Modifier.padding(start = 40.dp)) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Medium,
                lineHeight = 0.sp,
                fontFamily = Afacade
            )
            Text(
                text = brMoney.format(value),
                fontSize = 10.sp,
                color = if(value > 0f) Lime700 else if (value < 0f) Red400 else MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Normal,
                lineHeight = 0.sp,
                fontFamily = Ubuntu
            )
        }

    }
    Spacer(Modifier.height(5.dp))
}
package view.sidebar.components

import androidx.compose.desktop.ui.tooling.preview.Preview
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

@Preview
@Composable
fun GroupMenuItem(
    label: String,
    value: Float,
    onClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 20.dp)
            .height(35.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(25.dp).clip(RoundedCornerShape(8.dp)).pointerHoverIcon(PointerIcon.Hand).clickable { onClick() }) {
                Icon(
                    painter = painterResource("assets/icons/systemIcons/toggle_down.svg"),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(12.dp).align(Alignment.Center)
                )
            }
            Text(
                modifier = Modifier.padding(start = 7.dp),
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Medium,
                lineHeight = 0.sp,
                fontFamily = Afacade
            )
        }

        Text(
            modifier = Modifier.padding(0.dp),
            text = brMoney.format(value),
            fontSize = 10.sp,
            color = if (value > 0f) Lime700 else if (value < 0f) Red400 else MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Normal,
            lineHeight = 0.sp,
            fontFamily = Ubuntu
        )
    }
}
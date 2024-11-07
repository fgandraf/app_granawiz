package view.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.IconPaths
import view.theme.Afacade

@Composable
fun ClickableRow(
    iconResource: String = "",
    roundedBorder: Boolean = false,
    iconSize: DpSize = DpSize(12.dp, 12.dp),
    enabled: Boolean = true,
    label: String,
    onClick: () -> Unit
){
    var modifier = if (enabled)
        Modifier.clickable(onClick = onClick).pointerHoverIcon(PointerIcon.Hand)
    else
        Modifier

    if (roundedBorder) {
        modifier = modifier
            .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(8.dp))
            .background(Color.White)
    }


    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {

        if (iconResource != "") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Icon(
                    painter = painterResource(IconPaths.SYSTEM_ICONS + iconResource + ".svg"),
                    contentDescription = null,
                    tint = if (enabled) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.size(iconSize),
                )

            }
        }
        Text(modifier = Modifier.padding(end = 10.dp).fillMaxWidth(),
            text = label,
            textAlign = if (roundedBorder) TextAlign.Center else TextAlign.Start,
            fontSize  = 14.sp,
            color = if (enabled) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Medium,
            lineHeight = 0.sp,
            fontFamily = Afacade,
            fontStyle = if (enabled) FontStyle.Normal else FontStyle.Italic
        )
    }
}
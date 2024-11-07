package view.modules.categories.components

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
import config.IconPaths
import view.theme.Afacade

@Composable
fun AddButton(
    onClick: () -> Unit
){
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(25.dp)
            .clip(RoundedCornerShape(8.dp))
            .pointerHoverIcon(PointerIcon.Hand)
            .width(120.dp)
            .clickable { onClick() }
    ) {
        Icon(
            painter = painterResource(IconPaths.SYSTEM_ICONS + "plus.svg"),
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(15.dp),
        )

        Text(modifier = Modifier.padding(start = 10.dp),
            text = "Adicionar",
            fontSize  = 14.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Medium,
            lineHeight = 0.sp,
            fontFamily = Afacade
        )
    }
}
package view.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CaretLeft
import view.theme.ButtonPurple

@Composable
fun TransparentButton(
    modifier: Modifier = Modifier,
    text: String,
    textPadding: Dp = 5.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .pointerHoverIcon(PointerIcon.Hand)
            .width(80.dp)
            .height(35.dp)
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }, onClick = onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = PhosphorIcons.Light.CaretLeft,
                contentDescription = "Alert",
                tint = ButtonPurple,
                modifier = Modifier.size(14.dp)
            )
            Text(
                modifier = Modifier.padding(horizontal = textPadding),
                text = text,
                fontSize = 13.sp,
                color = ButtonPurple,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp,
                textAlign = TextAlign.Start
            )
        }

    }
}
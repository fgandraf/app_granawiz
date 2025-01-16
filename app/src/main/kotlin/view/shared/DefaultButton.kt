package view.shared

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.theme.ButtonPurple

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    confirmed: Boolean = true,
    text: String,
    textColor: Color = MaterialTheme.colors.primary,
    textPadding: Dp = 5.dp,
    shape: Shape = RoundedCornerShape(6.dp),
    color: Color = ButtonPurple,
    onClick: () -> Unit,
){
    Button(
        enabled = confirmed,
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        onClick = onClick,
        shape = shape,
        modifier = modifier
            .pointerHoverIcon(if(confirmed) PointerIcon.Hand else PointerIcon.Default),
    ){
        TextNormal(
            modifier = Modifier.padding(horizontal = textPadding),
            text = text,
            color = textColor,
            lineHeight = 16.sp
        )
    }
}
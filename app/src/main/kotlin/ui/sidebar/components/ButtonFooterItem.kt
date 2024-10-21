package ui.sidebar.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ButtonFooterItem(iconResource: String) {

    val secondaryColor = MaterialTheme.colors.secondary

    OutlinedButton(
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White.copy(alpha = 0.3f)),
        modifier = Modifier
            .pointerHoverIcon(PointerIcon.Hand)
            .height(23.dp)
            .width(75.dp),
        contentPadding = PaddingValues(4.dp),
        onClick = { }
    ) {
        Icon(
            painter = painterResource(iconResource),
            contentDescription = null,
            tint = secondaryColor,
            modifier = Modifier.fillMaxSize()
        )
    }
}
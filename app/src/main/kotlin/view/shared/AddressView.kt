package view.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.theme.Ubuntu

@Composable
fun AddressView(
    iconResource: String? = null,
    icon: ImageVector? = null,
    value: String,
    iconSize: DpSize = DpSize(17.dp, 17.dp),
    rootPath: Boolean? = false
){
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {

        if (rootPath == false)
            Text(modifier = Modifier.padding(end = 5.dp),
                text = "/",
                fontSize  = 13.sp,
                color = MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Normal,
                lineHeight = 0.sp,
                fontFamily = Ubuntu
            )

        if (icon != null)
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(iconSize),
            )
        else
            Icon(
                painter = painterResource(iconResource!!),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(iconSize),
            )
        Text(modifier = Modifier.padding(horizontal = 5.dp),
            text = value,
            fontSize  = 13.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Normal,
            lineHeight = 0.sp,
            fontFamily = Ubuntu
        )


    }
}
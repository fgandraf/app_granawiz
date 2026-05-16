package view.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.ArrowLeft
import domain.structs.PageAddress
import utils.rememberSvgPainter
import view.theme.DefaultFont

@Composable
fun DefaultScreenHeader(
    addresses: List<PageAddress>,
    backEnabled: Boolean = false,
    onBackClick: () -> Unit = {},
    subtitle: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 20.dp, end = 20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ClickableIcon(
                    enabled = backEnabled,
                    icon = PhosphorIcons.Bold.ArrowLeft,
                    iconSize = 22.dp,
                    boxSize = 25.dp
                ) { onBackClick() }
                Spacer(Modifier.width(10.dp))
                addresses.forEach { address ->
                    AddressView(
                        icon = address.iconVector,
                        iconSize = address.iconSize!!,
                        value = address.name,
                        rootPath = address.rootPath
                    )
                }
            }
            trailingContent?.invoke()
        }
        if (subtitle != null)
            TextNormal(
                text = subtitle,
                align = TextAlign.Start,
                modifier = Modifier.padding(top = 4.dp)
            )
    }
}

@Composable
fun AddressView(
    iconResource: String? = null,
    icon: ImageVector? = null,
    value: String,
    iconSize: DpSize = DpSize(17.dp, 17.dp),
    rootPath: Boolean? = false,
) {
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {

        if (rootPath == false)
            Text(
                modifier = Modifier.padding(end = 5.dp),
                text = "/",
                fontSize = 13.sp,
                color = MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Normal,
                lineHeight = 0.sp,
                fontFamily = DefaultFont
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
                painter = rememberSvgPainter(iconResource!!),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(iconSize),
            )
        Text(
            modifier = Modifier.padding(horizontal = 5.dp),
            text = value,
            fontSize = 13.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Normal,
            lineHeight = 0.sp,
            fontFamily = DefaultFont
        )
    }
}
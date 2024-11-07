package view.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import utils.rememberSvgPainter
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CaretRight
import utils.IconPaths

@Composable
fun DropDownTextField(
    modifier: Modifier = Modifier,
    icon: String? = null,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    value: String,
    label: String? = null,
    placeholder: String = "",
    showBorder: Boolean = true,
    borderOnActive: Boolean = false,
    onClick: () -> Unit,
) {

    var isFocused by remember { mutableStateOf(false) }
    val effectiveShowBorder = if (borderOnActive) isFocused else showBorder

    Column(modifier = modifier) {
        if (label != null)
            TextSmall(text = label, modifier = Modifier.padding(bottom = 5.dp))

        FocusableBox(
            showBorder = effectiveShowBorder,
            onClick = onClick,
            onFocusChange = if (borderOnActive) { focused -> isFocused = focused } else null,
        ) {
            if (value.isEmpty())
                TextSmall(
                    text = placeholder,
                    color = MaterialTheme.colors.primary.copy(alpha = 0.75f),
                    modifier = Modifier.fillMaxWidth()
                )
            else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = horizontalArrangement,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (icon != null && icon.isNotBlank())
                        Icon(
                            painter = rememberSvgPainter(IconPaths.CATEGORY_PACK + icon),
                            contentDescription = "Category Icon",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(15.dp)
                        )

                    TextNormal(text = value, modifier = Modifier.padding(start = 5.dp))
                }
            }

            Icon(
                imageVector = PhosphorIcons.Light.CaretRight,
                contentDescription = "click",
                modifier = Modifier.size(15.dp).align(Alignment.CenterEnd),
                tint = MaterialTheme.colors.primary
            )
        }
    }
}
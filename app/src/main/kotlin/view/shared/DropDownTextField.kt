package view.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CaretRight
import utils.IconPaths

@Composable
fun DropDownTextField(
    modifier: Modifier = Modifier,
    categoryIcon: String? = null,
    textAlign: TextAlign = TextAlign.Center,
    value: String,
    label: String? = null,
    placeholder: String = "",
    onClick: () -> Unit
){

    Column(modifier = modifier) {
        if (label != null)
            TextPrimary(text = label, modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)

        FocusableBox(onClick = onClick) {
            if (value.isEmpty())
                TextPrimary(
                    text = placeholder,
                    size = 10.sp,
                    align = textAlign,
                    color = MaterialTheme.colors.primary.copy(alpha = 0.75f),
                    modifier = Modifier.fillMaxWidth())
            else
            {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(IconPaths.CATEGORY_PACK + categoryIcon),
                        contentDescription = "Category Icon",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(15.dp)
                    )

                    TextPrimary(text = value, modifier = Modifier.padding(start = 5.dp))
                }
            }


            Icon(
                imageVector = PhosphorIcons.Light.CaretRight,
                contentDescription = "click",
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp)
            )
        }
    }
}
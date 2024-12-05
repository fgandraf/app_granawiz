package view.shared

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CaretDown
import com.adamglin.phosphoricons.light.CaretRight
import core.entity.Group

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupListComboBox(
    modifier: Modifier = Modifier,
    value: String,
    label: String? = null,
    placeholder: String,
    groupList: List<Group>,
    onClickItem: (Group) -> Unit = {}
){

    var borderSize by remember { mutableStateOf(0.dp) }
    var borderColor by remember { mutableStateOf(Color.Transparent) }
    val focused = remember { mutableStateOf(false) }

    if (focused.value) {
        borderSize = 1.2.dp
        borderColor = MaterialTheme.colors.secondary
    }else{
        borderSize = 1.dp
        borderColor = MaterialTheme.colors.primary
    }

    Column(modifier = modifier){
        var expanded by remember { mutableStateOf(false) }

        if (label != null)
            TextPrimary(text = label, modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)

        ExposedDropdownMenuBox(
            modifier = Modifier.onFocusChanged { state -> focused.value = state.isFocused },
            expanded = expanded, onExpandedChange = { expanded = !expanded }
        ) {

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .border(borderSize, borderColor, shape = RoundedCornerShape(5.dp))
                    .clip(RoundedCornerShape(5.dp))
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable { }
            ) {

                if (value.isBlank())
                    TextPrimary(
                        text = placeholder,
                        size = 10.sp,
                        color = MaterialTheme.colors.primary.copy(0.75f),
                        modifier = Modifier.padding(start = 10.dp)
                    )
                else
                    TextPrimary(text = value, modifier = Modifier.padding(start = 10.dp))


                Box(
                    Modifier.size(35.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) PhosphorIcons.Light.CaretDown else PhosphorIcons.Light.CaretRight,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(15.dp).align(Alignment.Center)
                    )
                }

            }

            ExposedDropdownMenu(
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                expanded = expanded, onDismissRequest = { expanded = false }
            ) {
                groupList.forEach { item ->
                    DropdownMenuItem( onClick = { onClickItem(item); expanded = !expanded } ) {
                        TextPrimary(text = item.name, size = 12.sp)
                    }
                }

            }

        }
    }
}
package view.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Plus
import com.adamglin.phosphoricons.light.Tag
import core.entity.Tag

@Composable
fun TagListView(
    modifier: Modifier = Modifier,
    tags: List<Tag>?,
    label: String? = null,
    placeholder: String = "",
    onClickTag: () -> Unit,
    onClickAdd: () -> Unit = {}
){
    val primaryColor = MaterialTheme.colors.primary

    Column(modifier = modifier) {
        if (label != null)
            TextPrimary(text = label, modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)

        Box(contentAlignment = Alignment.CenterStart,
            modifier = modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(start = 5.dp)
        ) {
            if (tags.isNullOrEmpty())
                TextPrimary(text = placeholder, size = 10.sp, color = primaryColor.copy(alpha = 0.75f), modifier = Modifier.fillMaxWidth())
            else{

                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    Row {
                        tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .clip(CircleShape)
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .clickable { onClickTag() },
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                ) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        imageVector = PhosphorIcons.Light.Tag,
                                        contentDescription = null,
                                        tint = primaryColor.copy(alpha = 0.75f)
                                    )
                                    TextPrimary(
                                        modifier = Modifier.padding(start = 5.dp),
                                        text = tag.name,
                                        size = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                            .clip(CircleShape)
                            .clickable { onClickAdd() }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)) {
                            Icon(
                                imageVector = PhosphorIcons.Light.Plus,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                            TextPrimary(
                                modifier = Modifier.padding(start = 5.dp),
                                text = "Adicionar",
                                size = 10.sp
                            )
                        }

                    }
                }
            }
        }
    }
}
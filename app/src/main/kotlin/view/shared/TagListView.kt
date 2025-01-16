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
    onClickAdd: () -> Unit = {}
){

    Column(modifier = modifier) {
        if (label != null)
            TextSmall(text = label, modifier = Modifier.padding(bottom = 5.dp))

        Box(contentAlignment = Alignment.CenterStart,
            modifier = modifier.fillMaxWidth().padding(start = 5.dp, top = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.weight(0.7f)) {
                    if (tags.isNullOrEmpty())
                        TextSmall(text = placeholder, color = MaterialTheme.colors.primary.copy(alpha = 0.75f))
                    else {
                        tags.forEach { tag ->
                            Box(modifier = Modifier.padding(end = 5.dp).clip(CircleShape)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                ) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        imageVector = PhosphorIcons.Light.Tag,
                                        contentDescription = null,
                                        tint = MaterialTheme.colors.primary.copy(alpha = 0.75f)
                                    )
                                    TextSmall(
                                        modifier = Modifier.padding(start = 5.dp),
                                        text = tag.name,
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand).clip(CircleShape).clickable { onClickAdd() }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)) {
                        Icon(
                            imageVector = PhosphorIcons.Light.Plus,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = MaterialTheme.colors.primary
                        )
                        TextSmall(
                            modifier = Modifier.padding(start = 5.dp),
                            text = "Adicionar",
                        )
                    }
                }
            }
        }
    }
}
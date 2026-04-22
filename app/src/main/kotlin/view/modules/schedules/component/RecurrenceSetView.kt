package view.modules.schedules.component

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
import com.adamglin.phosphoricons.light.PencilLine
import view.shared.TextSmall

@Composable
fun RecurrenceSetView(
    modifier: Modifier = Modifier,
    summary: String,
    label: String? = null,
    onClickEdit: () -> Unit = {},
) {
    Column(modifier = modifier) {
        if (label != null)
            TextSmall(text = label, modifier = Modifier.padding(bottom = 5.dp))

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.fillMaxWidth().padding(start = 5.dp, top = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    TextSmall(
                        text = summary,
                        color = MaterialTheme.colors.primary.copy(alpha = 0.75f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .clip(CircleShape)
                        .clickable { onClickEdit() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = PhosphorIcons.Light.PencilLine,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = MaterialTheme.colors.primary
                        )
                        TextSmall(
                            modifier = Modifier.padding(start = 5.dp),
                            text = "Editar",
                        )
                    }
                }
            }
        }
    }
}

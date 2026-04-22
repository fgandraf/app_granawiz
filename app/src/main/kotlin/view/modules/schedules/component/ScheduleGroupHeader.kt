package view.modules.schedules.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import view.shared.TextH1

@Composable
fun ScheduleGroupHeader(
    modifier: Modifier = Modifier,
    title: String,
) {
    val corners = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    Box(
        modifier
            .offset(y = 0.5.dp)
            .padding(start = 90.dp)
    ) {
        Box(
            Modifier
                .clip(corners)
                .border(0.5.dp, MaterialTheme.colors.onSurface, corners)
                .zIndex(1f)
        ) {
            TextH1(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                text = title
            )
        }
    }
}

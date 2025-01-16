package core.structs

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

data class PageAddress(
    val iconVector: ImageVector,
    val iconSize: DpSize? = DpSize(17.dp, 17.dp),
    val name: String,
    val rootPath: Boolean? = false,
)

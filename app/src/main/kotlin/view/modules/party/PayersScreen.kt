package view.modules.party

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.HandArrowDown
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.nav_payers
import domain.enums.PartyType
import domain.structs.PageAddress
import org.jetbrains.compose.resources.stringResource
import view.modules.party.components.Body
import view.shared.DefaultScreenHeader

@Composable
fun PayersScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 15.dp, bottom = 15.dp)
            .border(1.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colors.surface)
    ) {

        //===== HEADER
        DefaultScreenHeader(
            addresses = listOf(
                PageAddress(
                    iconVector = PhosphorIcons.Regular.HandArrowDown,
                    iconSize = DpSize(21.dp, 18.dp),
                    name = stringResource(Res.string.nav_payers),
                    rootPath = true
                )
            )
        )

        //===== BODY
        Body(PartyType.PAYER)

    }
}
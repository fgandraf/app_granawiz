package view.modules.party

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.regular.HandArrowUp
import core.enums.PartyType
import view.modules.party.components.Body
import view.shared.AddressView
import view.shared.ClickableIcon

@Composable
fun ReceiversScreen() {

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        //===== HEADER
        Column{
            Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                ClickableIcon(enabled = false, icon = PhosphorIcons.Bold.ArrowLeft, iconSize = 22.dp, boxSize = 25.dp){ }
                Spacer(Modifier.width(10.dp))
                Row { AddressView(icon = PhosphorIcons.Regular.HandArrowUp, iconSize = DpSize(21.dp, 18.dp),value = "Beneficiários", rootPath = true ) }
            }
        }

        //===== BODY
        Body(PartyType.RECEIVER)
    }
}
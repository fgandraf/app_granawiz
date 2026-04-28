package view.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Check
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Warning
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import domain.entity.Party
import org.jetbrains.compose.resources.stringResource
import view.theme.DefaultFont
import view.theme.RedWarning

@Composable
fun DialogReassignAndDelete(
    title: String,
    icon: ImageVector,
    party: Party,
    partyTypeName: String,
    otherParties: List<Party>,
    onConfirm: (Party) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedParty by remember { mutableStateOf<Party?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(500.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
        ) {
            DialogTitleBar(title = title, onCloseRequest = onDismiss)
            Divider(Modifier.background(MaterialTheme.colors.onSurface))

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    text = party.name,
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Medium,
                    fontFamily = DefaultFont,
                    fontSize = 16.sp,
                )
            }
            Divider(Modifier.padding(horizontal = 10.dp).background(MaterialTheme.colors.onSurface))

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 15.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .border(1.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(5.dp))
                        .background(Color.Yellow)
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = PhosphorIcons.Regular.Warning,
                        contentDescription = null,
                        tint = RedWarning
                    )
                    TextNormal(
                        modifier = Modifier.padding(start = 10.dp),
                        text = stringResource(Res.string.irreversible_warning),
                        color = RedWarning
                    )
                }

                if (otherParties.isEmpty()) {
                    TextNormal(
                        text = stringResource(Res.string.delete_party_no_other_parties, partyTypeName),
                        color = MaterialTheme.colors.primary,
                        align = TextAlign.Justify,
                        lineHeight = 16.sp
                    )
                } else {
                    TextNormal(
                        text = stringResource(Res.string.delete_party_has_transactions, partyTypeName),
                        color = MaterialTheme.colors.primary,
                        align = TextAlign.Justify,
                        lineHeight = 16.sp
                    )
                    TextSmall(
                        text = stringResource(Res.string.delete_party_reassign_label),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 180.dp)
                            .border(1.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                    ) {
                        items(otherParties) { target ->
                            val isSelected = selectedParty?.id == target.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.08f)
                                        else Color.Transparent
                                    )
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .clickable { selectedParty = target }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextNormal(text = target.name)
                                if (isSelected)
                                    Icon(
                                        imageVector = PhosphorIcons.Light.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colors.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                            }
                            if (target.id != otherParties.last().id)
                                Divider(Modifier.background(MaterialTheme.colors.onSurface))
                        }
                    }
                }
            }

            Divider(Modifier.background(MaterialTheme.colors.onSurface))

            Column(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (otherParties.isNotEmpty()) {
                    DefaultButton(
                        modifier = Modifier.fillMaxWidth(),
                        confirmed = selectedParty != null,
                        color = RedWarning,
                        text = stringResource(Res.string.delete_party_reassign_button),
                        textColor = Color.White
                    ) {
                        onConfirm(selectedParty!!)
                    }
                }
                DefaultButton(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.secondaryVariant,
                    text = stringResource(Res.string.cancel),
                    onClick = onDismiss,
                )
            }
        }
    }
}

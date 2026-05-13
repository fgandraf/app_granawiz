package view.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Warning
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.confirm_delete_input_label
import com.felipegandra.generated.resources.irreversible_warning
import org.jetbrains.compose.resources.stringResource
import view.theme.DefaultFont
import view.theme.RedWarning


@Composable
fun DialogDelete(
    title: String = "",
    icon: ImageVector,
    objectName: String,
    alertText: String,
    onClickButton: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(500.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
        ) {

            //===== Title Bar
            DialogTitleBar(title = title, onCloseRequest = onDismiss)
            Divider(Modifier.background(MaterialTheme.colors.onSurface))

            //===== Information
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = icon,
                    contentDescription = "Object icon",
                    tint = MaterialTheme.colors.primary
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    text = objectName,
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Medium,
                    fontFamily = DefaultFont,
                    fontSize = 16.sp,
                )
            }
            Divider(Modifier.padding(horizontal = 10.dp).background(MaterialTheme.colors.onSurface))

            //===== Alert
            Column(
                modifier = Modifier.fillMaxWidth().padding(15.dp, 25.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .border(1.dp, MaterialTheme.colors.onSurface, shape = RoundedCornerShape(5.dp))
                        .background(Color.Yellow.copy(0.8f))
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = PhosphorIcons.Regular.Warning,
                        contentDescription = "Exclamation icon",
                        tint = Color.Black
                    )
                    TextNormal(
                        modifier = Modifier.padding(start = 10.dp),
                        text = stringResource(Res.string.irreversible_warning),
                        color = Color.Black
                    )
                }
                Spacer(Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp)
                ) {
                    Divider(Modifier.height(50.dp).width(3.dp).background(MaterialTheme.colors.onSurface))
                    Spacer(Modifier.width(25.dp))
                    TextNormal(
                        text = alertText,
                        color = MaterialTheme.colors.primary,
                        align = TextAlign.Justify,
                        lineHeight = 16.sp
                    )
                }
            }
            Divider(Modifier.padding(horizontal = 10.dp).background(MaterialTheme.colors.onSurface))

            //===== Confirmation
            var value by remember { mutableStateOf("") }
            val confirmed by remember { derivedStateOf { value == objectName } }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp, horizontal = 40.dp)
            ) {
                TextNormal(
                    modifier = Modifier.padding(bottom = 5.dp),
                    text = stringResource(Res.string.confirm_delete_input_label, objectName),
                    align = TextAlign.Start
                )
                DefaultTextField(
                    value = value,
                    onValueChange = { value = it },
                )
            }
            Divider(Modifier.background(MaterialTheme.colors.onSurface))

            //===== Delete Button
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 20.dp)
            ) {
                DefaultButton(
                    modifier = Modifier.fillMaxWidth(),
                    confirmed = confirmed,
                    color = RedWarning,
                    text = title,
                    textColor = Color.White
                ) {
                    onClickButton()
                    onDismiss()
                }
            }
        }
    }
}
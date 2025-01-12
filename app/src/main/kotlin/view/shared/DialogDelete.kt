package view.shared

import androidx.compose.foundation.Image
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
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Warning
import view.theme.RedWarning
import view.theme.Ubuntu


@Composable
fun DialogDelete(
    title: String = "",
    icon: ImageVector,
    objectName: String,
    alertText: String,
    onClickButton: () -> Unit,
    onDismiss: () -> Unit
){
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(420.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
        ) {

            //===== Title Bar
            DialogTitleBar(title = title, onCloseRequest = onDismiss)
            Divider(Modifier.background(MaterialTheme.colors.onSurface))

            //===== Information
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
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
                    fontFamily = Ubuntu,
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
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .border(1.dp, MaterialTheme.colors.onSurface, shape = RoundedCornerShape(5.dp))
                        .background(Color.Yellow.copy(alpha = 0.2F))
                ){
                    Image(
                        modifier = Modifier.size(16.dp),
                        imageVector = PhosphorIcons.Light.Warning,
                        contentDescription = "Exclamation icon"
                    )
                    TextPrimary(
                        modifier = Modifier.padding(start = 10.dp),
                        text = "Esta ação é irreversível! Leia com atenção!",
                        color = RedWarning,
                        weight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.height(20.dp))

                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp)
                ) {
                    Divider(Modifier.height(50.dp).width(3.dp).background(MaterialTheme.colors.onSurface))
                    Spacer(Modifier.width(25.dp))
                    TextPrimary(
                        text = alertText,
                        color = MaterialTheme.colors.secondary,
                        align = TextAlign.Justify,
                        lineHeight = 16.sp
                    )
                }
            }
            Divider(Modifier.padding(horizontal = 10.dp).background(MaterialTheme.colors.onSurface))

            //===== Confirmation
            var value by remember{mutableStateOf("")}
            val confirmed by remember { derivedStateOf { value == objectName } }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp, horizontal = 40.dp)
            ){
                TextPrimary(
                    modifier = Modifier.padding(bottom = 5.dp),
                    text = "Digite \"${objectName}\"",
                    size = 12.sp,
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
            ){
                DefaultButton(modifier = Modifier.fillMaxWidth(), confirmed = confirmed, color = RedWarning, text = title, textColor = Color.White){
                    onClickButton()
                    onDismiss()
                }
            }
        }
    }
}
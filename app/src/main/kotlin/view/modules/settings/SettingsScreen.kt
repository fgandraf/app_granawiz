package view.modules.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import view.modules.UserPreferences.isLightTheme
import view.shared.DialogTitleBar
import view.shared.TextH2
import view.shared.TextNormal
import view.theme.Afacade

@Composable
fun SettingsScreen(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(520.dp)
                .height(600.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
        ) {

            //===== Title Bar
            DialogTitleBar(title = "", onCloseRequest = onDismiss)

            //===== Logo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource("assets/images/icon.svg"),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )

                Text(
                    text = "GranaWiz",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Afacade,
                    style = TextStyle(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF73378a),
                                MaterialTheme.colors.secondary,
                                Color(0xFF73378a)
                            )
                        )
                    )
                )
            }
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically,) {
                Switch(checked = isLightTheme, onCheckedChange = {isLightTheme = !isLightTheme })
                Spacer(Modifier.width(10.dp))
                TextNormal(text = if (isLightTheme) "Apagar" else "Acender")
            }

            Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp).background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.2f)))



            //===== Main
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp, end = 30.dp, bottom = 30.dp, start = 30.dp)
                    .background(MaterialTheme.colors.background)
            ) {

                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

                    TextH2(text = "Em desenvolvimento...")

                }
            }

        }
    }
}
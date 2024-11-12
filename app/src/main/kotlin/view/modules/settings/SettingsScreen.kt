package view.modules.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
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
import view.shared.DialogTitleBar
import view.theme.Afacade
import view.theme.Gray200

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
            DialogTitleBar("", onDismiss)

            //===== Logo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            ) {
                Image(
                    painter = painterResource("assets/images/MoneyMapIcon.png"),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )

                Text(
                    text = "MoneyMap",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Afacade,
                    style = TextStyle(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF73378a),
                                Color(0xFF0e0036),
                                Color(0xFF73378a)
                            )
                        )
                    )
                )
            }
            Divider(Modifier.padding(horizontal = 30.dp))



            //===== Main
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp, end = 30.dp, bottom = 30.dp, start = 30.dp)
                    .background(Gray200)
            ) {
                //Implements
            }



        }
    }
}
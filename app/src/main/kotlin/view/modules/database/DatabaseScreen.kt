package view.modules.database

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.*
import view.shared.AddressView
import view.shared.TextPrimary
import view.theme.*

@Composable
fun DatabaseScreen() {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        //===== HEADER
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row { AddressView(icon = PhosphorIcons.Light.Database, value = "Banco de Dados") }
            }
        }

        //===== BODY
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                DatabaseCard(icon = PhosphorIcons.Light.Shapes, color = BluePale, text = "Categorias") {  }

                Spacer(modifier = Modifier.width(10.dp))

                DatabaseCard(icon = PhosphorIcons.Light.Tag, color = YellowPale, text = "Etiquetas") {  }
                Spacer(modifier = Modifier.width(10.dp))

                DatabaseCard(icon = PhosphorIcons.Light.HandArrowUp, color = Red200, text = "Beneficiários") {  }

                Spacer(modifier = Modifier.width(10.dp))

                DatabaseCard(icon = PhosphorIcons.Light.HandArrowDown, color = Lime200, text = "Pagadores") {  }

            }




        }
    }
}

@Composable
fun DatabaseCard(
    color: Color,
    icon: ImageVector,
    text: String,
    content: @Composable ()-> Unit = {}
){
    Card(
        modifier = Modifier.width(200.dp).height(300.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        //backgroundColor = color
    ) {
        Box(Modifier.fillMaxSize()) {

            Icon(imageVector = icon, contentDescription = "", Modifier.size(120.dp).align(Alignment.Center), tint = color)



            Box(Modifier.fillMaxWidth().height(40.dp).background(color).align(Alignment.BottomCenter), contentAlignment = Alignment.Center) {
                TextPrimary(fontFamily = Afacade, text = text, color = Color.White, size = 20.sp, weight = FontWeight.Normal, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
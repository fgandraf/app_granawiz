package ui.sidebar

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.sidebar.components.*
import ui.theme.Gray200
import ui.theme.Ubuntu

@Preview
@Composable
fun Sidebar() {
    Column(modifier = Modifier.width(260.dp).fillMaxHeight().background(Gray200).drawBehind {
        drawLine(
            color = MaterialTheme.colors.primaryVariant,
            start = Offset(279.5.dp.toPx(), 0f),
            end = Offset(279.5.dp.toPx(), size.height),
            strokeWidth = 0.5.dp.toPx()
        )}
    ){

        //===== HEADER
        val totalAmount by remember { mutableStateOf("R$ 2.125,00") }
        Box(modifier = Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 15.dp)) {
            Icon(
                painter = painterResource("icons/system/settings.svg"),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .pointerHoverIcon(PointerIcon.Hand)
                    .align(Alignment.CenterStart)
                    .clickable { /*TO DO*/ },
            )
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(modifier = Modifier.fillMaxWidth(),
                    text = totalAmount,
                    fontSize  = 16.sp,
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp,
                    fontFamily = Ubuntu,
                    textAlign = TextAlign.Center
                )
                Text(modifier = Modifier.fillMaxWidth(),
                    text = "SALDO TOTAL",
                    fontSize  = 8.sp,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 12.sp,
                    fontFamily = Ubuntu,
                    textAlign = TextAlign.Center
                )
            }
        }
        Divider()

        //===== MAIN
        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {

            Spacer(modifier = Modifier.height(25.dp))
            MenuItem(iconResource = "icons/system/dashboard.svg", label = "Dashboard") { /*TO DO*/ }
            MenuItem(iconResource = "icons/system/calendar.svg", label = "Agendamentos") { /*TO DO*/ }
            MenuItem(iconResource = "icons/system/report.svg", label = "Relatórios") { /*TO DO*/ }

            Spacer(modifier = Modifier.height(15.dp))

            SectionTitle("Base de dados")
            MenuItem(iconResource = "icons/system/category.svg", label = "Categorias") { /*TO DO*/ }
            MenuItem(iconResource = "icons/system/tag.svg", label = "Etiquetas") { /*TO DO*/ }
            MenuItem(iconResource = "icons/system/recipient.svg", label = "Recebedores") { /*TO DO*/ }
            MenuItem(iconResource = "icons/system/payer.svg", label = "Pagadores") { /*TO DO*/ }

            Spacer(modifier = Modifier.height(15.dp))

            SectionTitle("Transações")
            MenuItem(iconResource = "icons/system/list.svg", label = "Todas as transações") { /*TO DO*/ }

            GroupMenuItem("Pessoal", 1025f) { /*TO DO*/ }
            AccountMenuItem(iconResource = "icons/logos/nubank.svg", label = "NuBank", value = 1025.00f) { /*TO DO*/ }
            AccountMenuItem(iconResource = "icons/logos/inter.svg", label = "Inter", value = 500.00f) { /*TO DO*/ }
            AccountMenuItem(iconResource = "icons/logos/bb.svg", label = "Banco do Brasil", value = -400.0f) { /*TO DO*/ }

            GroupMenuItem("Empresa", 200f) { }
            AccountMenuItem(iconResource = "icons/logos/cef.svg", label = "Caixa Econômica", value = 200f) { /*TO DO*/ }
            AccountMenuItem(iconResource = "icons/logos/mastercard.svg", label = "Cartão Master", value = 0f) { /*TO DO*/ }
        }

        //===== FOOTER
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 15.dp)
        ) {
            ButtonFooterItem("icons/system/add_group.svg") { /*TO DO*/ }
            ButtonFooterItem("icons/system/add_account.svg") { /*TO DO*/ }
            ButtonFooterItem("icons/system/arrange.svg") { /*TO DO*/ }
        }
    }
}
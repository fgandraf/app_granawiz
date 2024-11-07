package view.modules.sidebar.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import config.IconPaths
import model.entity.account.BankAccount
import viewModel.SidebarViewModel
import view.shared.ClickableIcon
import view.shared.DefaultTextField
import view.shared.TextPrimary
import view.theme.Red200
import view.theme.Red400
import view.theme.Ubuntu

@Composable
fun DialogDeleteAccount(
    account: BankAccount,
    viewModel: SidebarViewModel,
    onDismiss: () -> Unit
){
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(420.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
        ) {

            //===== Title
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(5.dp).padding(start = 5.dp)
            ) {
                TextPrimary(
                    text = "Excluir conta ${account.group.name} / ${account.name}",
                    size = 12.sp,
                    color = MaterialTheme.colors.secondary,
                )
                ClickableIcon(
                    icon = "close",
                    padding = true,
                    color = MaterialTheme.colors.secondary,
                    onClick = onDismiss
                )
            }
            Divider()

            //===== Account Information
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(IconPaths.BANK_LOGOS + account.icon),
                    contentDescription = "${account.name} logo"
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    text = "${account.group.name}/${account.name}",
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Ubuntu,
                    fontSize = 16.sp,
                )
            }
            Divider(Modifier.padding(horizontal = 10.dp))

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
                        .border(1.dp, Red200.copy(0.5f), shape = RoundedCornerShape(5.dp))
                        .background(Color.Yellow.copy(alpha = 0.2F))
                ){
                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(IconPaths.SYSTEM_ICONS + "alert.svg"),
                        contentDescription = "${account.name} logo"
                    )
                    TextPrimary(
                        modifier = Modifier.padding(start = 10.dp),
                        text = "Esta ação é irreversível! Leia com atenção!",
                        color = Red400,
                        weight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.height(20.dp))

                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp)
                ) {
                    Divider(Modifier.height(50.dp).width(3.dp))
                    Spacer(Modifier.width(25.dp))
                    TextPrimary(
                        text = "Isso irá excluir permanentemente a conta ${account.group.name} → ${account.name}, bem como todas as transações associadas a ela.",
                        color = MaterialTheme.colors.secondary,
                        align = TextAlign.Justify,
                        lineHeight = 16.sp
                    )
                }
            }
            Divider(Modifier.padding(horizontal = 10.dp))

            //===== Confirmation
            var value by remember{mutableStateOf("")}
            val confirmed by remember { derivedStateOf { value == "${account.group.name}/${account.name}" } }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(20.dp)
            ){
                TextPrimary(
                    modifier = Modifier.padding(bottom = 5.dp),
                    text = "Para confirmar, digite \"${account.group.name}/${account.name}\" abaixo:",
                    size = 12.sp,
                    align = TextAlign.Start
                )
                DefaultTextField(
                    borderColor = if (confirmed) Color.Blue else Color.Red,
                    value = value,
                    onValueChange = { value = it },
                )
            }
            Divider()

            //===== Delete Button
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 20.dp)
            ){
                Button(
                    modifier = Modifier.fillMaxWidth().pointerHoverIcon(if(confirmed) PointerIcon.Hand else PointerIcon.Default),
                    enabled = confirmed,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    onClick = {
                        viewModel.deleteAccount(account)
                        onDismiss()
                    }
                ){
                    Text(
                        text = "Excluir conta",
                        color = Color.White,
                        fontStyle = if(!confirmed) FontStyle.Italic else FontStyle.Normal,
                        fontFamily = Ubuntu,
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}
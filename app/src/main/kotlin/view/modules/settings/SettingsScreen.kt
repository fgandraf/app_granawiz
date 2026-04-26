package view.modules.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import utils.rememberSvgPainter
import view.modules.UserPreferences
import view.modules.UserPreferences.isLightTheme
import view.shared.*
import view.theme.Afacade
import view.theme.ButtonPurple
import view.theme.Ubuntu
import viewModel.SettingsViewModel
import java.awt.Desktop
import java.net.URI

private val CURRENCIES = listOf(
    "$" to "Argentine Peso ($)",
    "$" to "Australian Dollar ($)",
    "R$" to "Brazilian Real (R$)",
    "£" to "British Pound (£)",
    "$" to "Canadian Dollar ($)",
    "$" to "Chilean Peso ($)",
    "¥" to "Chinese Yuan (¥)",
    "$" to "Colombian Peso ($)",
    "Kč" to "Czech Koruna (Kč)",
    "kr" to "Danish Krone (kr)",
    "E£" to "Egyptian Pound (E£)",
    "€" to "Euro (€)",
    "$" to "Hong Kong Dollar ($)",
    "Ft" to "Hungarian Forint (Ft)",
    "₹" to "Indian Rupee (₹)",
    "Rp" to "Indonesian Rupiah (Rp)",
    "₪" to "Israeli New Shekel (₪)",
    "¥" to "Japanese Yen (¥)",
    "$" to "Mexican Peso ($)",
    "$" to "New Zealand Dollar ($)",
    "₦" to "Nigerian Naira (₦)",
    "kr" to "Norwegian Krone (kr)",
    "₨" to "Pakistani Rupee (₨)",
    "S/." to "Peruvian Sol (S/.)",
    "₱" to "Philippine Peso (₱)",
    "zł" to "Polish Zloty (zł)",
    "lei" to "Romanian Leu (lei)",
    "₽" to "Russian Ruble (₽)",
    "﷼" to "Saudi Riyal (﷼)",
    "$" to "Singapore Dollar ($)",
    "R" to "South African Rand (R)",
    "₩" to "South Korean Won (₩)",
    "kr" to "Swedish Krona (kr)",
    "Fr" to "Swiss Franc (Fr)",
    "$" to "Taiwan Dollar ($)",
    "฿" to "Thai Baht (฿)",
    "₺" to "Turkish Lira (₺)",
    "د.إ" to "UAE Dirham (د.إ)",
    "₴" to "Ukrainian Hryvnia (₴)",
    "$" to "US Dollar ($)",
    "₫" to "Vietnamese Dong (₫)",
)

@Composable
fun SettingsScreen(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = remember { SettingsViewModel() },
) {
    var currencyMenuExpanded by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(520.dp)
                .height(700.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
        ) {
            DialogTitleBar(title = "Configurações", onCloseRequest = onDismiss)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 30.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = rememberSvgPainter("assets/images/icon.svg"),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(Modifier.height(4.dp))
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
                    Spacer(Modifier.height(2.dp))
                    TextSmall(text = "0.0.1-Beta", italic = true)
                }

                Divider(color = MaterialTheme.colors.onSurface)

                // Aparência
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextH4(text = "APARÊNCIA")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = isLightTheme,
                            onCheckedChange = { viewModel.setTheme(!isLightTheme) }
                        )
                        Spacer(Modifier.width(10.dp))
                        TextNormal(text = if (isLightTheme) "Apagar" else "Acender")
                    }
                }

                Divider(color = MaterialTheme.colors.onSurface)

                // Moeda
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextH4(text = "MOEDA")
                    Box {
                        val selectedLabel = CURRENCIES.find { it.second == UserPreferences.currencyLabel }?.second
                            ?: CURRENCIES.find { it.first == UserPreferences.currencyLabel }?.second
                            ?: UserPreferences.currencyLabel
                        DropDownTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = selectedLabel,
                            label = "Moeda padrão",
                            onClick = { currencyMenuExpanded = true }
                        )
                        DropdownMenu(
                            expanded = currencyMenuExpanded,
                            onDismissRequest = { currencyMenuExpanded = false },
                        ) {
                            CURRENCIES.forEach { (_, label) ->
                                DropdownMenuItem(onClick = {
                                    viewModel.setCurrencySymbol(label)
                                    currencyMenuExpanded = false
                                }) {
                                    TextNormal(text = label)
                                }
                            }
                        }
                    }
                }

                Divider(color = MaterialTheme.colors.onSurface)

                // Sobre
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    TextH4(text = "SOBRE")
                    Spacer(Modifier.height(2.dp))
                    InfoRow(label = "Desenvolvedor", value = "Felipe Gandra")
                    InfoRow(
                        label = "Site",
                        value = "www.felipegandra.com",
                        isLink = true,
                        onClick = { Desktop.getDesktop().browse(URI("https://www.felipegandra.com")) }
                    )
                    InfoRow(label = "Distribuição", value = "Gratuita")
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                TextSmall(text = "© 2026 Felipe Gandra. Todos os direitos reservados.")
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    isLink: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextNormal(text = label, color = MaterialTheme.colors.primaryVariant)
        if (isLink) {
            Text(
                text = value,
                fontSize = 12.sp,
                color = ButtonPurple,
                fontWeight = FontWeight.Normal,
                fontFamily = Ubuntu,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable(onClick = onClick)
                    .pointerHoverIcon(PointerIcon.Hand)
            )
        } else {
            TextNormal(text = value)
        }
    }
}

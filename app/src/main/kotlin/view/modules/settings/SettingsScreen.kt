package view.modules.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CaretDown
import com.adamglin.phosphoricons.light.CaretUp
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import utils.rememberSvgPainter
import view.shared.DialogTitleBar
import view.shared.TextH4
import view.shared.TextNormal
import view.shared.TextSmall
import view.theme.Afacade
import view.theme.ButtonPurple
import view.theme.DefaultFont
import viewModel.SettingsViewModel
import viewModel.UserPreferences
import viewModel.UserPreferences.isLightTheme
import java.awt.Desktop
import java.net.URI

private val CURRENCIES = mapOf(
    "Argentine Peso ($)"      to "$",
    "Australian Dollar ($)"   to "$",
    "Brazilian Real (R$)"     to "R$",
    "British Pound (£)"       to "£",
    "Canadian Dollar ($)"     to "$",
    "Chilean Peso ($)"        to "$",
    "Chinese Yuan (¥)"        to "¥",
    "Colombian Peso ($)"      to "$",
    "Czech Koruna (Kč)"       to "Kč",
    "Danish Krone (kr)"       to "kr",
    "Egyptian Pound (E£)"     to "E£",
    "Euro (€)"                to "€",
    "Hong Kong Dollar ($)"    to "$",
    "Hungarian Forint (Ft)"   to "Ft",
    "Indian Rupee (₹)"        to "₹",
    "Indonesian Rupiah (Rp)"  to "Rp",
    "Israeli New Shekel (₪)"  to "₪",
    "Japanese Yen (¥)"        to "¥",
    "Mexican Peso ($)"        to "$",
    "New Zealand Dollar ($)"  to "$",
    "Nigerian Naira (₦)"      to "₦",
    "Norwegian Krone (kr)"    to "kr",
    "Pakistani Rupee (₨)"     to "₨",
    "Peruvian Sol (S/.)"      to "S/.",
    "Philippine Peso (₱)"     to "₱",
    "Polish Zloty (zł)"       to "zł",
    "Romanian Leu (lei)"      to "lei",
    "Russian Ruble (₽)"       to "₽",
    "Saudi Riyal (﷼)"         to "﷼",
    "Singapore Dollar ($)"    to "$",
    "South African Rand (R)"  to "R",
    "South Korean Won (₩)"    to "₩",
    "Swedish Krona (kr)"      to "kr",
    "Swiss Franc (Fr)"        to "Fr",
    "Taiwan Dollar ($)"       to "$",
    "Thai Baht (฿)"           to "฿",
    "Turkish Lira (₺)"        to "₺",
    "UAE Dirham (د.إ)"        to "د.إ",
    "Ukrainian Hryvnia (₴)"   to "₴",
    "US Dollar ($)"           to "$",
    "Vietnamese Dong (₫)"     to "₫",
)

private val CURRENCY_FORMATS = mapOf(
    "dot-comma" to "1.234,56",
    "comma-dot" to "1,234.56",
    "plain-dot" to "1234.56",
)

private val LANGUAGES = mapOf(
    "pt-BR" to "Português (Brasil)",
    "en-US" to "English (US)",
)

@Composable
fun SettingsScreen(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = remember { SettingsViewModel() },
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(520.dp)
                .height(700.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
        ) {
            DialogTitleBar(title = stringResource(Res.string.settings_title), onCloseRequest = onDismiss)

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
                        text = stringResource(Res.string.settings_app_name),
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
                    TextSmall(text = stringResource(Res.string.settings_app_version), italic = true)
                }

                Divider(color = MaterialTheme.colors.onSurface)


                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {

                    val themes = mapOf(
                        "light" to stringResource(Res.string.settings_theme_light),
                        "dark"  to stringResource(Res.string.settings_theme_dark),
                    )
                    val currentTheme = if (isLightTheme) "light" else "dark"

                    SettingsItem(
                        label = stringResource(Res.string.settings_theme_label),
                        value = themes[currentTheme] ?: themes.values.first()
                    ) { onDismiss ->
                        themes.forEach { (key, label) ->
                            DropdownMenuItem(onClick = {
                                viewModel.setTheme(key == "light")
                                onDismiss()
                            }) {
                                TextNormal(text = label)
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically){

                    val titleBarStyles = mapOf(
                        "default" to stringResource(Res.string.settings_titlebar_style_default),
                        "macos"   to stringResource(Res.string.settings_titlebar_style_macos),
                    )
                    SettingsItem(
                        label = stringResource(Res.string.settings_titlebar_style_label),
                        value = titleBarStyles[UserPreferences.titleBarStyle] ?: titleBarStyles.values.first()
                    ) { onDismiss ->
                        titleBarStyles.forEach { (key, label) ->
                            DropdownMenuItem(onClick = {
                                viewModel.setTitleBarStyle(key)
                                onDismiss()
                            }) {
                                TextNormal(text = label)
                            }
                        }
                    }
                }



                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    SettingsItem(
                        label = stringResource(Res.string.settings_language),
                        value = LANGUAGES[UserPreferences.language] ?: LANGUAGES.values.first()
                    ) { onDismiss ->
                        LANGUAGES.forEach { (tag, label) ->
                            DropdownMenuItem(onClick = {
                                viewModel.setLanguage(tag)
                                onDismiss()
                            }) {
                                TextNormal(text = label)
                            }
                        }
                    }
                }


                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    val selectedCurrency = when {
                        UserPreferences.currencyLabel in CURRENCIES -> UserPreferences.currencyLabel
                        else -> CURRENCIES.entries.find { it.value == UserPreferences.currencyLabel }?.key
                            ?: UserPreferences.currencyLabel
                    }
                    SettingsItem(
                        label = stringResource(Res.string.settings_default_currency),
                        value = selectedCurrency
                    ) { onDismiss ->
                        CURRENCIES.keys.forEach { label ->
                            DropdownMenuItem(onClick = {
                                viewModel.setCurrencySymbol(label)
                                onDismiss()
                            }) {
                                TextNormal(text = label)
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    SettingsItem(
                        label = stringResource(Res.string.settings_number_format),
                        value = CURRENCY_FORMATS[UserPreferences.currencyFormat] ?: CURRENCY_FORMATS.values.first()
                    ) { onDismiss ->
                        CURRENCY_FORMATS.forEach { (key, label) ->
                            DropdownMenuItem(onClick = {
                                viewModel.setCurrencyFormat(key)
                                onDismiss()
                            }) {
                                TextNormal(text = label)
                            }
                        }
                    }
                }


                Divider(color = MaterialTheme.colors.onSurface)

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    TextH4(text = stringResource(Res.string.settings_section_about))
                    Spacer(Modifier.height(2.dp))
                    InfoRow(label = stringResource(Res.string.settings_developer), value = stringResource(Res.string.settings_developer_name))
                    InfoRow(
                        label = stringResource(Res.string.settings_site),
                        value = stringResource(Res.string.settings_site_url),
                        isLink = true,
                        onClick = { Desktop.getDesktop().browse(URI("https://www.felipegandra.com")) }
                    )
                    InfoRow(label = stringResource(Res.string.settings_distribution), value = stringResource(Res.string.settings_distribution_value))
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                TextSmall(text = stringResource(Res.string.settings_copyright))
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
                fontFamily = DefaultFont,
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


@Composable
private fun SettingsItem(
    label: String,
    value: String,
    dropdownContent: @Composable (onDismiss: () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        Row(Modifier.weight(0.5f)) { TextH4(text = "$label:") }

        BoxWithConstraints(Modifier.weight(1f)) {
            var focused by remember { mutableStateOf(false) }

            Column {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                        .pointerHoverIcon(PointerIcon.Hand)
                        .onFocusChanged { focusState -> focused = focusState.isFocused}
                        .clickable { expanded = true }
                        .padding(horizontal = 10.dp)
                ) {
                    if (!value.isEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextNormal(text = value, modifier = Modifier.padding(start = 5.dp))
                        }
                    }

                    Icon(
                        imageVector = if (expanded) PhosphorIcons.Light.CaretUp else PhosphorIcons.Light.CaretDown,
                        contentDescription = "click",
                        modifier = Modifier.size(15.dp).align(Alignment.CenterEnd),
                        tint = MaterialTheme.colors.primary
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .wrapContentHeight()
                    .width(maxWidth)
            ) {
                dropdownContent { expanded = false }
            }
        }
    }
}
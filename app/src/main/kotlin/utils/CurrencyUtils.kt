package utils

import java.text.NumberFormat
import java.util.Locale
import view.modules.UserPreferences

val brMoney: NumberFormat = NumberFormat.getCurrencyInstance().apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
}

val toBrMoney: NumberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt-BR")).apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
}

val toUsMoney: NumberFormat = NumberFormat.getNumberInstance(Locale.US).apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
}

val toPlainDot: NumberFormat = NumberFormat.getNumberInstance(Locale.US).apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
    isGroupingUsed = false
}

private fun numberFormatter(): NumberFormat = when (UserPreferences.currencyFormat) {
    "comma-dot" -> toUsMoney
    "plain-dot" -> toPlainDot
    else -> toBrMoney
}

fun formatNumber(amount: Double): String = numberFormatter().format(amount)

fun formatCurrency(amount: Double, symbol: String): String = "$symbol ${numberFormatter().format(amount)}"

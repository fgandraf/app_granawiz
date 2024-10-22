package ui

import java.text.NumberFormat

val brMoney: NumberFormat = NumberFormat.getCurrencyInstance().apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
}
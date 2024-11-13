package model.utils

import java.text.NumberFormat

val brMoney: NumberFormat = NumberFormat.getCurrencyInstance().apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
}

val toBrMoney: NumberFormat = NumberFormat.getNumberInstance().apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
}

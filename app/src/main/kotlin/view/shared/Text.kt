package view.shared

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import view.theme.Afacade
import view.theme.GrayPrimaryDark
import view.theme.GrayPrimaryLight
import view.theme.Ubuntu


@Composable
fun TextNormal(
    modifier: Modifier = Modifier,
    text: String,
    color: Color? = if (MaterialTheme.colors.isLight) GrayPrimaryLight else GrayPrimaryDark,
    align: TextAlign = TextAlign.Start,
    lineHeight: TextUnit? = 12.sp,
    fontStyle: FontStyle? = FontStyle.Normal,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 12.sp,
        color = color!!,
        fontWeight = FontWeight.Normal,
        lineHeight = lineHeight!!,
        fontFamily = Ubuntu,
        textAlign = align,
        fontStyle = fontStyle,
    )
}

@Composable
fun TextMedium(
    modifier: Modifier = Modifier,
    text: String,
    color: Color? = if (MaterialTheme.colors.isLight) GrayPrimaryLight else GrayPrimaryDark,
    align: TextAlign = TextAlign.Start,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 14.sp,
        color = color!!,
        fontWeight = FontWeight.Medium,
        lineHeight = 14.sp,
        fontFamily = Ubuntu,
        textAlign = align
    )
}

@Composable
fun TextSmall(
    modifier: Modifier = Modifier,
    color: Color? = if (MaterialTheme.colors.isLight) GrayPrimaryLight else GrayPrimaryDark,
    text: String,
    align: TextAlign = TextAlign.Start,
    italic: Boolean = false,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 10.sp,
        color = color!!,
        fontWeight = FontWeight.Medium,
        lineHeight = 10.sp,
        fontFamily = Ubuntu,
        textAlign = align,
        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal
    )
}


@Composable
fun TextH1(
    modifier: Modifier = Modifier,
    text: String,
    color: Color? = if (MaterialTheme.colors.isLight) GrayPrimaryLight else GrayPrimaryDark,
    align: TextAlign = TextAlign.Start,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 26.sp,
        color = color!!,
        fontWeight = FontWeight.Bold,
        lineHeight = 26.sp,
        fontFamily = Afacade,
        textAlign = align
    )
}


@Composable
fun TextH2(
    modifier: Modifier = Modifier,
    text: String,
    align: TextAlign = TextAlign.Start,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 16.sp,
        color = if (MaterialTheme.colors.isLight) GrayPrimaryLight else GrayPrimaryDark,
        fontWeight = FontWeight.Bold,
        lineHeight = 16.sp,
        fontFamily = Afacade,
        textAlign = align
    )
}


@Composable
fun TextH3(
    modifier: Modifier = Modifier,
    text: String,
    color: Color? = if (MaterialTheme.colors.isLight) GrayPrimaryLight else GrayPrimaryDark,
    align: TextAlign = TextAlign.Start,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 14.sp,
        color = color!!,
        fontWeight = FontWeight.Medium,
        lineHeight = 14.sp,
        fontFamily = Afacade,
        textAlign = align
    )
}


@Composable
fun TextH4(
    modifier: Modifier = Modifier,
    color: Color? = if (MaterialTheme.colors.isLight) GrayPrimaryLight else GrayPrimaryDark,
    text: String,
    align: TextAlign = TextAlign.Start,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 11.sp,
        color = color!!,
        fontWeight = FontWeight.Bold,
        lineHeight = 10.sp,
        fontFamily = Afacade,
        textAlign = align
    )
}
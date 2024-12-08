package view.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Check
import com.adamglin.phosphoricons.light.Plus
import com.adamglin.phosphoricons.light.X
import view.theme.Afacade

@Composable
fun AddListItem(
    isVisible: MutableState<Boolean>,
    value: MutableState<String>,
    icon: ImageVector? = null,
    confirmationClick: () -> Unit,
    alertDialogContent: @Composable () -> Unit? = {}
){
    Column {
        AnimatedVisibility(visible = isVisible.value) {

            val focusRequester = remember { FocusRequester() }
            Column {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(30.dp)
                ) {
                    Row {
                        if (icon != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxHeight().width(40.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = Color.Blue,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = if (icon != null) 0.dp else 10.dp)
                        ) {
                            BasicTextField(
                                value = value.value,
                                onValueChange = { value.value = it },
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 0.sp,
                                    fontFamily = Afacade,
                                    color = Color.Blue
                                ),
                                modifier = Modifier.focusRequester(focusRequester)
                            )
                        }
                    }
                    Row(modifier = Modifier.padding(end = 10.dp)) {
                        ClickableIcon(
                            icon = PhosphorIcons.Light.X,
                            color = Color.Blue,
                            shape = RoundedCornerShape(6.dp),
                            onClick = { isVisible.value = false; value.value = "" }
                        )
                        ClickableIcon(
                            icon = PhosphorIcons.Light.Check,
                            color = Color.Blue,
                            shape = RoundedCornerShape(6.dp),
                            onClick = { confirmationClick(); isVisible.value = false; value.value = "" }
                        )
                    }
                }
                Divider(Modifier.padding(horizontal = 15.dp))
            }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
        }
        AnimatedVisibility(visible = !isVisible.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(end = 10.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier.width(130.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .height(25.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .pointerHoverIcon(PointerIcon.Hand)
                            .width(120.dp)
                            .clickable { isVisible.value = true }
                    ) {
                        Icon(
                            imageVector = PhosphorIcons.Light.Plus,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(15.dp),
                        )

                        Text(modifier = Modifier.padding(start = 10.dp),
                            text = "Adicionar",
                            fontSize  = 14.sp,
                            color = MaterialTheme.colors.primary,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 0.sp,
                            fontFamily = Afacade
                        )
                    }
                }
            }
        }
    }

    alertDialogContent()
}
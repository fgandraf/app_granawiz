package view.modules.receivers.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.modules.categories.components.AddButton
import view.shared.ClickableIcon
import view.theme.Afacade

@Composable
fun AddListItem(
    isVisible: MutableState<Boolean>,
    value: MutableState<String>,
    confirmationClick: () -> Unit,
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
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 10.dp)
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
                            icon = "close",
                            color = Color.Blue,
                            shape = RoundedCornerShape(6.dp),
                            iconSize = 12.dp,
                            padding = true,
                            onClick = { isVisible.value = false; value.value = "" }
                        )
                        ClickableIcon(
                            icon = "check",
                            color = Color.Blue,
                            shape = RoundedCornerShape(6.dp),
                            iconSize = 12.dp,
                            padding = true,
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
                    AddButton { isVisible.value = true }
                }
            }
        }
    }
}
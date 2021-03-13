@file:Suppress("DEPRECATION")

package com.example.jetchatdemo.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.jetchatdemo.BackPressHandler
import com.example.jetchatdemo.R
import kotlinx.coroutines.launch

enum class InputSelector { NONE }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserInput(
    onMessageSent: (String) -> Unit,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    var currentInputSelector by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    val dismissKeyboard = { currentInputSelector = InputSelector.NONE }
    if (currentInputSelector != InputSelector.NONE) {
        BackPressHandler(onBackPressed = dismissKeyboard)
    }
    var textState by remember { mutableStateOf(TextFieldValue()) }
    var textFieldFocusState by remember { mutableStateOf(false) }
    Column(modifier) {
        Divider()
        UserInputText(
            textFieldValue = textState,
            onTextChanged = { textState = it },
            keyboardShown = currentInputSelector == InputSelector.NONE && textFieldFocusState,
            onTextFieldFocused = { focused ->
                if (focused) {
                    currentInputSelector = InputSelector.NONE
                    scope.launch {
                        scrollState.animateScrollTo(0)
                    }
                }
                textFieldFocusState = focused
            },
            onSentClick = {
                if (textState.text == "") return@UserInputText
                onMessageSent(textState.text)
                textState = TextFieldValue()
                scope.launch {
                    scrollState.animateScrollTo(0)
                }
                dismissKeyboard()
            }
        )
    }
}

val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey

@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    keyboardShown: Boolean,
    onTextFieldFocused: (Boolean) -> Unit,
    onSentClick: () -> Unit,
) {
    val description = stringResource(id = R.string.textfield_desc)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = description
                keyboardShownProperty = keyboardShown
            },
        horizontalArrangement = Arrangement.End
    ) {
        Surface {
            val image = painterResource(id = R.drawable.icon_bottom_bar)
            val surfaceRatio = image.intrinsicSize.width / image.intrinsicSize.height
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(surfaceRatio)
                    .align(Alignment.Bottom)
            ) {
                Image(
                    painter = image,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .aspectRatio(surfaceRatio)
                        .fillMaxWidth(),
                    contentDescription = stringResource(id = R.string.attached_image)
                )
                Row(
                    Modifier.align(Alignment.CenterStart),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        Modifier
                            .height(5.dp)
                            .weight(13f)
                    )
                    var lastFocusState by remember { mutableStateOf(FocusState.Inactive) }
                    BasicTextField(
                        value = textFieldValue,
                        onValueChange = { onTextChanged(it) },
                        modifier = Modifier
                            .weight(38f)
                            .fillMaxWidth()
                            .onFocusChanged { state ->
                                if (lastFocusState != state) {
                                    onTextFieldFocused(state == FocusState.Active)
                                }
                                lastFocusState = state
                            },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = keyboardType,
                            imeAction = ImeAction.Send
                        ),
                        maxLines = 1,
                        cursorBrush = SolidColor(LocalContentColor.current),
                        textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
                    )
                    Box(
                        Modifier
                            .height(5.dp)
                            .weight(8f)
                    )
                    val sentRatio =
                        image.intrinsicSize.width * 9 / 68 / image.intrinsicSize.height
                    Box(
                        Modifier
                            .aspectRatio(sentRatio)
                            .weight(9f)
                            .clickable(
                                onClick = { onSentClick() },
                                role = Role.Image
                            )
                    )
                }
            }
        }
    }
}
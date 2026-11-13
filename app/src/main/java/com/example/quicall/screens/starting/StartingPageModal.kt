package com.example.quicall.screens.starting

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quicall.R
import com.example.quicall.screens.components.CombinedActionDialButton
import com.example.quicall.ui.theme.Shapes
import kotlinx.coroutines.awaitCancellation

private val dialPadKeys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartingPageModal(
    modifier: Modifier = Modifier,
    viewModel: StartingPageViewModel = viewModel(),
    sheetState: SheetState,
    onCallAttempt: (String) -> Unit = {},
    isExpanded: Boolean = false
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Toast.makeText(
            context,
            configuration.screenWidthDp.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    val dialPadContent: @Composable () -> Unit = {
        DialPadLayout(
            modifier = modifier,
            dialDigits = uiState.dialDigits,
            onDialDigitsChanged = viewModel::onDialDigitsChanged,
            onDialDeleteButtonPressed = viewModel::onDialDeleteButtonPressed,
            onDialButtonPressed = viewModel::onDialButtonPressed,
            phoneNumberToCall = viewModel::phoneNumberToCall,
            onCallAttempt = onCallAttempt
        )
    }

    StartingPageModalContent(
        modifier = modifier,
        sheetState = sheetState,
        isExpanded = isExpanded,
        dialPadContent = dialPadContent,
        onDialDismissed = {
            viewModel.onDialShow(false)
        },
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartingPageModalContent(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    isExpanded: Boolean,
    dialPadContent: @Composable () -> Unit = {},
    onDialDismissed: () -> Unit = {},
) {

    if (isExpanded) {
        dialPadContent()
    } else {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = {
                onDialDismissed()
            },
            sheetState = sheetState
        ) {

            dialPadContent()


        }
    }
}

@Composable
private fun DialPadLayout(
    modifier: Modifier = Modifier,
    dialDigits: TextFieldValue,
    onDialDigitsChanged: (TextFieldValue) -> Unit,
    onDialDeleteButtonPressed: () -> Unit = {},
    onDialButtonPressed: (String) -> Unit = {},
    phoneNumberToCall: (String) -> Unit = {},
    onCallAttempt: (String) -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier) {
        val dialButtonRatio = when {
            maxWidth < 400.dp -> 2f
            maxWidth < 600.dp -> 2.5f
            else -> 3f
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxHeight)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.End
            ) {

                DisableSoftKeyboard {
                    TextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = dimensionResource(R.dimen.text_dial_padding)),
                        value = dialDigits,
                        onValueChange = {
                            onDialDigitsChanged(it)
                        },
                        singleLine = true,
                        maxLines = 1,
                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                    )
                }

                IconButton(
                    onClick = {
                        onDialDeleteButtonPressed()
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.btn_icon_size)),
                        painter = painterResource(R.drawable.ic_delete_digit),
                        contentDescription = null
                    )
                }
            }

            FlowRow(
                maxItemsInEachRow = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = dimensionResource(R.dimen.btn_dial_drawer_padding)),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.Center
            ) {
                dialPadKeys.forEach { digit ->
                    key(digit) {
                        if (digit == "0") {
                            CombinedActionDialButton(
                                onClick = {
                                    onDialButtonPressed(digit)
                                },
                                onLongClick = {
                                    onDialButtonPressed("+")
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(dimensionResource(R.dimen.btn_dial_digit_padding))
                                    .aspectRatio(dialButtonRatio),
                                shape = Shapes.medium,
                                secondDigit = "+"
                            ) {
                                Text(digit, fontSize = 20.sp)
                            }
                        } else {
                            Button(
                                onClick = {
                                    onDialButtonPressed(digit)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(dimensionResource(R.dimen.btn_dial_digit_padding))
                                    .aspectRatio(dialButtonRatio),
                                shape = Shapes.medium,
                            ) {
                                Text(digit, fontSize = 20.sp)
                            }
                        }

                    }
                }
            }

            IconButton(
                enabled = dialDigits.text.isNotBlank(),
                onClick = {
                    phoneNumberToCall(dialDigits.text)
                    onCallAttempt(dialDigits.text)
                },
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.btn_dial_call_padding))
                    .width(140.dp)
                    .height(56.dp)
                    .aspectRatio(2f),
                shape = Shapes.medium,
                colors = IconButtonDefaults.iconButtonColors(Color(0xFF00FF88))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_call),
                        contentDescription = null
                    )
                    Text(stringResource(R.string.call_btn))
                }
            }
        }

    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DisableSoftKeyboard(content: @Composable () -> Unit) {
    InterceptPlatformTextInput(
        interceptor = { _, _ ->
            // 🔒 Παγώνει και ακυρώνει το αίτημα του συστήματος να σηκώσει το πληκτρολόγιο
            awaitCancellation()
        },
        content = content
    )
}
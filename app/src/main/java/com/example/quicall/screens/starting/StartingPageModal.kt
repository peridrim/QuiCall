package com.example.quicall.screens.starting

import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quicall.R
import com.example.quicall.ui.theme.Shapes
import kotlinx.coroutines.awaitCancellation

private val dialPadKeys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartingPageModal(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onCallAttempt: (String) -> Unit = {},
    onDialDismissed: () -> Unit = {},
    dialDigits: TextFieldValue,
    onDialDigitsChanged: (TextFieldValue) -> Unit,
    onDialDeleteButtonPressed: () -> Unit = {},
    onDialButtonPressed: (String) -> Unit = {},
    phoneNumberToCall: (String) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val dialButtonRatio = if (isPortrait) 2f else 4f


    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            onDialDismissed()
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
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
//                Text(
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(horizontal = dimensionResource(R.dimen.text_dial_padding)),
//                    text = uiState.dialDigits.take(10),
//                    softWrap = false,
//                    textAlign = TextAlign.End,
//                    style = MaterialTheme.typography.headlineMedium
//                )

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
                        Button(
                            onClick = {
                                onDialButtonPressed(digit)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(dimensionResource(R.dimen.btn_dial_digit_padding))
                                .aspectRatio(dialButtonRatio),
                            shape = Shapes.medium
                        ) {
                            Text(digit, fontSize = 20.sp)
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
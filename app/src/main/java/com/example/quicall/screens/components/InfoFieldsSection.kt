package com.example.quicall.screens.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.quicall.R
import com.example.quicall.data.FormField
import com.example.quicall.screens.add_contact.AddContactUiState

@Composable
fun InfoFieldsSection(
    modifier: Modifier = Modifier,
    uiState: AddContactUiState,
    onBackClicked: () -> Unit = {},
    onTextFieldValueChange: (String, FormField) -> Unit = { _, _ -> },
    showTextFieldError: (Boolean, Boolean, FormField) -> Unit = { _, _, _ ->},
    onSaveContact: () -> Unit = {}
) {
    val nameFieldText = stringResource(R.string.text_field_label_name)
    val lastNameFieldText = stringResource(R.string.text_field_label_lastname)
    val phoneNumberFieldText = stringResource(R.string.text_field_label_phone_number)
    val emailFieldText = stringResource(R.string.text_field_label_email)

    val textFieldModifier = Modifier
        .fillMaxWidth()
        .padding(all = dimensionResource(R.dimen.text_field_padding))
    val keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            modifier = textFieldModifier,
            value = uiState.name,
            onValueChange = { newValue -> onTextFieldValueChange(newValue, FormField.NAME) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = null
                )
            },
            label = { Text(text = nameFieldText) },
            singleLine = true,
            isError = uiState.isNameFieldEmpty,
            supportingText = { if (uiState.isSupportNameTextVisible) Text(stringResource(R.string.support_text_field_empty_name)) },
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onNext = {
                    if (uiState.name.isNotBlank()) {
                        showTextFieldError(false, false, FormField.NAME)
                        focusManager.moveFocus(FocusDirection.Next)
                    } else {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                }
            )
        )

        OutlinedTextField(
            modifier = textFieldModifier,
            value = uiState.lastName,
            onValueChange = { newValue -> onTextFieldValueChange(newValue, FormField.LAST_NAME) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = null
                )
            },
            label = { Text(text = lastNameFieldText) },
            singleLine = true,
            isError = false,
            supportingText = {},
            keyboardOptions = keyboardOptions
        )

        OutlinedTextField(
            modifier = textFieldModifier,
            value = uiState.phoneNumber,
            onValueChange = { newValue -> onTextFieldValueChange(newValue, FormField.PHONE_NUMBER) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_phone_number),
                    contentDescription = null
                )
            },
            label = { Text(text = phoneNumberFieldText) },
            singleLine = true,
            isError = uiState.isPhoneNumberFieldEmpty,
            supportingText = { if (uiState.isSupportPhoneNumberTextVisible) Text(stringResource(R.string.support_text_field_empty_phone_number)) },
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onNext = {
                    if (uiState.phoneNumber.isNotBlank()) {
                        showTextFieldError(false, false, FormField.PHONE_NUMBER)
                        focusManager.moveFocus(FocusDirection.Next)
                    } else {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                }
            )
        )

        OutlinedTextField(
            modifier = textFieldModifier,
            value = uiState.email,
            onValueChange = { newValue -> onTextFieldValueChange(newValue, FormField.EMAIL) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = null
                )
            },
            label = { Text(text = emailFieldText) },
            singleLine = true,
            isError = false,
            supportingText = {},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

        Row(
            modifier = textFieldModifier,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.btn_save_cancel_space))
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(3f),
                onClick = {
                        onSaveContact()
                }
            ) {
                Text(stringResource(R.string.save_contact_btn))
            }

            Button(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(3f),
                onClick = { onBackClicked() }
            ) {
                Text(stringResource(R.string.cancel_contact_btn))
            }
        }
    }
}
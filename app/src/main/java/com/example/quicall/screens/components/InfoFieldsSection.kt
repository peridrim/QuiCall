package com.example.quicall.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.quicall.R
import com.example.quicall.screens.add_contact.AddContactUiState

@Composable
fun InfoFieldsSection(
    modifier: Modifier = Modifier,
    uiState: AddContactUiState,
    onBackClicked: () -> Unit = {},
) {
    val textFieldModifier = Modifier
        .fillMaxWidth()
        .padding(all = dimensionResource(R.dimen.text_field_padding))
    val keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            modifier = textFieldModifier,
            value = uiState.name,
            onValueChange = {},
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = null
                )
            },
            label = { Text(text = "Name") },
            singleLine = true,
            isError = false,
            supportingText = {},
            keyboardOptions = keyboardOptions
        )

        OutlinedTextField(
            modifier = textFieldModifier,
            value = uiState.lastName,
            onValueChange = {},
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = null
                )
            },
            label = { Text(text = "Last Name") },
            singleLine = true,
            isError = false,
            supportingText = {},
            keyboardOptions = keyboardOptions
        )

        OutlinedTextField(
            modifier = textFieldModifier,
            value = uiState.phoneNumber,
            onValueChange = {},
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_phone_number),
                    contentDescription = null
                )
            },
            label = { Text(text = "Phone Number") },
            singleLine = true,
            isError = false,
            supportingText = {},
            keyboardOptions = keyboardOptions
        )

        OutlinedTextField(
            modifier = textFieldModifier,
            value = uiState.email,
            onValueChange = {},
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = null
                )
            },
            label = { Text(text = "E-mail") },
            singleLine = true,
            isError = false,
            supportingText = {},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    //TODO clear focus
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
                onClick = {}
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
package com.example.quicall.screens.add_contact

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.quicall.R
import com.example.quicall.data.FormField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddContactPageViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AddContactUiState())
    val uiState = _uiState.asStateFlow()


    fun onTextFieldValueChange(newValue: String, field: FormField) {

        _uiState.update{ currentState ->
            when (field) {
                FormField.NAME -> { currentState.copy(name = newValue) }

                FormField.LAST_NAME -> { currentState.copy(lastName = newValue) }

                FormField.PHONE_NUMBER -> { currentState.copy(phoneNumber = newValue) }

                FormField.EMAIL -> { currentState.copy(email = newValue) }
            }
        }
    }

}
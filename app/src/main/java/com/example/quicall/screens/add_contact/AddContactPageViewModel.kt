package com.example.quicall.screens.add_contact

import androidx.lifecycle.ViewModel
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
                FormField.NAME -> {
                    currentState.copy(
                        name = newValue,
                        isSupportNameTextVisible = false,
                        isNameFieldEmpty = false
                    )
                }

                FormField.LAST_NAME -> { currentState.copy(lastName = newValue) }

                FormField.PHONE_NUMBER -> {
                        currentState.copy(
                        phoneNumber = newValue,
                        isSupportPhoneNumberTextVisible = false,
                        isPhoneNumberFieldEmpty = false
                    )
                }

                FormField.EMAIL -> { currentState.copy(email = newValue) }

                else -> currentState
            }
        }
    }

    fun showTextFieldError(isTextVisible: Boolean, isFieldEmpty: Boolean, field: FormField = FormField.UNSPECIFIED) {
        _uiState.update { currentState ->
            when (field) {
                FormField.NAME -> {
                    currentState.copy(
                        isSupportNameTextVisible = isTextVisible,
                        isNameFieldEmpty = isFieldEmpty
                    )
                }
                FormField.PHONE_NUMBER -> {
                    currentState.copy(
                        isSupportPhoneNumberTextVisible = isTextVisible,
                        isPhoneNumberFieldEmpty = isFieldEmpty
                    )
                }
                else -> currentState.copy(
                    isSupportNameTextVisible = false,
                    isSupportPhoneNumberTextVisible = false,
                    isNameFieldEmpty = false,
                    isPhoneNumberFieldEmpty = false
                )
            }
        }
    }

    fun onSaveContact() {
        val currentState = _uiState.value
        val isNameEmpty = currentState.name.isBlank()
        val isPhoneNumberEmpty = currentState.phoneNumber.isBlank()

        when {
            isNameEmpty && isPhoneNumberEmpty -> {
                showTextFieldError(isTextVisible = true, isFieldEmpty = true, field = FormField.NAME)
                showTextFieldError(isTextVisible = true, isFieldEmpty = true, field = FormField.PHONE_NUMBER)
            }

            isNameEmpty -> showTextFieldError(isTextVisible = true, isFieldEmpty = true, field = FormField.NAME)

            isPhoneNumberEmpty -> showTextFieldError(isTextVisible = true, isFieldEmpty = true, field = FormField.PHONE_NUMBER)

            else -> showTextFieldError(isTextVisible = false, isFieldEmpty = false, field = FormField.UNSPECIFIED)
        }

        //TODO: Save contact
    }
}
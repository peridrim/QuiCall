package com.example.quicall.screens.add_contact

import android.net.Uri
import com.example.quicall.R
import com.example.quicall.data.FormField

data class AddContactUiState(
    val name: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val imageResourceId: Int? = 0,
    val selectedImageUri: Uri? = null,
    val isNameFieldEmpty: Boolean = false,
    val isPhoneNumberFieldEmpty: Boolean = false,
    val isSupportNameTextVisible: Boolean = false,
    val isSupportPhoneNumberTextVisible: Boolean = false,
)

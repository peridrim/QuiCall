package com.example.quicall.screens.add_contact

import com.example.quicall.R
import com.example.quicall.data.FormField

data class AddContactUiState(
    val name: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val imageResourceId: Int? = R.drawable.test_img,
    val isNameFieldEmpty: Boolean = false,
    val isPhoneNumberFieldEmpty: Boolean = false,
    val isSupportNameTextVisible: Boolean = false,
    val isSupportPhoneNumberTextVisible: Boolean = false,
)

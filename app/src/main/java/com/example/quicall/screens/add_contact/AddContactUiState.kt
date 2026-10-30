package com.example.quicall.screens.add_contact

import com.example.quicall.R

data class AddContactUiState(
    val name: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val imageResourceId: Int? = R.drawable.test_img,
)

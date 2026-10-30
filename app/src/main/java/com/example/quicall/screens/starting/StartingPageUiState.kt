package com.example.quicall.screens.starting

import androidx.compose.ui.text.input.TextFieldValue
import com.example.quicall.data.model.Contact

data class StartingPageUiState(
    val name: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val imageResourceId: Int? = null,
    val contacts: List<Contact> = emptyList(),
    val filteredContacts: List<Contact> = emptyList(),
    val contactToRemove: Contact? = null,
    val searchQuery: String = "",
    val dialDigits: TextFieldValue = TextFieldValue(""),
    val dialSheetState: Boolean = false,
    val selectedPhoneNumber: String = "",
)

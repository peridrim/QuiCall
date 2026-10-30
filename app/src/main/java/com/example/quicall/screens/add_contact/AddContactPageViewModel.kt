package com.example.quicall.screens.add_contact

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddContactPageViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AddContactUiState())
    val uiState = _uiState.asStateFlow()


}
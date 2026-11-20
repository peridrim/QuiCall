package com.example.quicall.screens.starting

import android.content.Context
import android.content.Intent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.quicall.data.DataSource
import com.example.quicall.data.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.Normalizer
import java.util.regex.Pattern

class StartingPageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StartingPageUiState())
    val uiState: StateFlow<StartingPageUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            val initialContacts = DataSource.dummyContacts
            currentState.copy(
                contacts = initialContacts,
                filteredContacts = initialContacts
            )
        }
    }

    fun removeContact() {
        val contactToDelete = _uiState.value.contactToRemove ?: return
        _uiState.update { currentState ->
            val updatedContacts = currentState.contacts.filter { it.id != contactToDelete.id }
            val updatedFilteredContacts = currentState.filteredContacts.filter { it.id != contactToDelete.id }
            currentState.copy(
                contacts = updatedContacts,
                filteredContacts = updatedFilteredContacts
            )
        }
    }

    fun onDialShow(dialSheetState: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(dialSheetState = dialSheetState)
        }
    }

    fun hideRemoveDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                contactToRemove = null
            )
        }
    }
    fun showRemoveDialog(contact: Contact) {
        _uiState.update { currentState ->
            currentState.copy(
                contactToRemove = contact
            )
        }
    }

    fun onDialButtonPressed(digit: String) {
        val currentSelection = _uiState.value.dialDigits.selection
        val currentText = _uiState.value.dialDigits.text

        if (currentText.length < 14) {
            val beforeSelection = currentText.substring(0, currentSelection.start)
            val afterSelection = currentText.substring(currentSelection.end)

            val newText = beforeSelection + digit + afterSelection

            val newCursorPosition = currentSelection.start + digit.length

            _uiState.update { currentState ->
                currentState.copy(
                    dialDigits = TextFieldValue(
                        text = newText,
                        selection = TextRange(newCursorPosition)
                    )
                )
            }
        }
    }

    fun removeDialDigits() {
        _uiState.update { currentState ->
            currentState.copy(
                dialDigits = TextFieldValue("")
            )
        }
    }

    fun onDialDigitsChanged(newValue: TextFieldValue) {
        // Επιτρέπουμε την αλλαγή ΜΟΝΟ αν το κείμενο δεν ξεπερνάει τα 10 ψηφία
        if (newValue.text.length <= 10) {
            _uiState.update { currentState ->
                currentState.copy(dialDigits = newValue)
            }
        }
    }

    fun onDialDeleteButtonPressed() {
        val currentSelection = _uiState.value.dialDigits.selection
        val currentText = _uiState.value.dialDigits.text

        if (currentText.isNotBlank() && currentSelection.start > 0) {
            val beforeSelection = currentText.substring(0, currentSelection.start - 1)
            val afterSelection = currentText.substring(currentSelection.end)

            val newText = beforeSelection + afterSelection

            val newCursorPosition = currentSelection.start - 1

            _uiState.update { currentState ->
                currentState.copy(
//                dialDigits = currentState.dialDigits.dropLast(1)
                    dialDigits = TextFieldValue(
                        text = newText,
                        selection = TextRange(newCursorPosition)
                    )
                )
            }
        }


    }

    fun makeCall(context: Context, phoneNumber: String) {
//        val phoneNumber = _uiState.value.dialDigits.text

        if (phoneNumber.isNotBlank()) {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = "tel:$phoneNumber".toUri()
            }
            context.startActivity(intent)
        }
    }

    fun phoneNumberToCall(selectedPhoneNumber: String) {
        _uiState.update { currentState ->
            currentState.copy(selectedPhoneNumber = selectedPhoneNumber)
        }
    }

    fun addContact(contact: Contact) {
        //TODO: add contact form list
    }

    fun onQueryChange(currentText: String) {    //Creates a filtered list of contacts based on the current text
        val cleanedCurrentText = currentText.normalizeForSearch()

        val allContacts = _uiState.value.contacts
        val filteredList = if (currentText.isBlank())
            allContacts
        else {
            allContacts.filter { item ->
                item.firstName.normalizeForSearch().contains(cleanedCurrentText, ignoreCase = true) ||
                        item.lastName.normalizeForSearch().contains(cleanedCurrentText, ignoreCase = true)
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = currentText,
                filteredContacts = filteredList
            )
        }
    }
}

fun String.normalizeForSearch(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(temp)
        .replaceAll("")
        .lowercase()
//        .replace("ς", "σ")
}
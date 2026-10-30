package com.example.quicall.screens.starting

import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.quicall.R
import com.example.quicall.data.DataSource
import com.example.quicall.data.model.Contact
import com.example.quicall.screens.components.ContactCard
import com.example.quicall.screens.components.TopBar
import com.example.quicall.ui.theme.AnimationConstants
import com.example.quicall.ui.theme.QuiCallTheme

@Composable
fun StartingPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: StartingPageViewModel = viewModel(),
    onCallAttempt: (String) -> Unit = {},
    ) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StartingPageContent(
        modifier = modifier,
        onCallAttempt = onCallAttempt,
        queryText = uiState.searchQuery,
        onQueryChange = { viewModel.onQueryChange(it) },
        filteredContacts = uiState.filteredContacts,
        showRemoveDialog = { contact -> viewModel.showRemoveDialog(contact) },
        contactToRemove = uiState.contactToRemove,
        hideRemoveDialog = { viewModel.hideRemoveDialog() },
        removeContact = { viewModel.removeContact() },
        phoneNumberToCall = { phoneNumber -> viewModel.phoneNumberToCall(phoneNumber) }
    )


}

@Composable
fun StartingPageContent(
    modifier: Modifier,
    onCallAttempt: (String) -> Unit = {},
    queryText: String = "",
    onQueryChange: (String) -> Unit = {},
    filteredContacts: List<Contact> = emptyList(),
    showRemoveDialog: (Contact) -> Unit = {},
    hideRemoveDialog: () -> Unit = {},
    contactToRemove: Contact? = null,
    removeContact: () -> Unit = {},
    phoneNumberToCall: (String) -> Unit = {}
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val cardWidth = configuration.screenWidthDp
    val cardHeight = configuration.screenHeightDp

    val calculatedWidth = (cardWidth * 0.9f).dp
    val calculatedHeight = if (isPortrait) (cardHeight * 0.2f).dp else (cardHeight * 0.4f).dp

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                Modifier
                    .fillMaxWidth()
                    .padding(top = statusBarPadding),
                queryText = queryText,
                onQueryChange = { onQueryChange(it) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(
                items = filteredContacts,
                key = { contact -> contact.id }
            ) { contact ->
                ContactCard(
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.card_vertical_padding))
                        .animateItem(
                            fadeInSpec = tween(AnimationConstants.CONTACT_CARD_FADE_IN),
                            fadeOutSpec = tween(AnimationConstants.CONTACT_CARD_FADE_OUT),
                            placementSpec = spring(
                                Spring.DampingRatioNoBouncy,
                                Spring.StiffnessLow
                            )
                        )
                        .width(calculatedWidth)
                        .height(calculatedHeight),
                    onCallAttempt = onCallAttempt,
                    phoneNumberToCall = phoneNumberToCall,
                    name = contact.firstName,
                    lastName = contact.lastName,
                    phoneNumber = contact.phoneNumber,
                    imageResourceId = contact.imageResId,
                    onRemoveContact = { showRemoveDialog(contact) }
                )
            }
        }

    }

    if (contactToRemove != null) {
        AlertDialog(
            onDismissRequest = { hideRemoveDialog() },
            confirmButton = {
                TextButton(
                    onClick = {
                        removeContact()
                        hideRemoveDialog()
                    }
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { hideRemoveDialog() }
                ) {
                    Text(text = stringResource(R.string.no))
                }
            },
            title = { Text(text = stringResource(R.string.delete_contact)) },
        )
    }
}

@Preview
@Composable
fun StartingPagePreview() {
    QuiCallTheme {
        StartingPageContent(
            Modifier.fillMaxSize(),
            filteredContacts = DataSource.dummyContacts
        )
    }
}
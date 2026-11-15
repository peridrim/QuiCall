package com.example.quicall.screens.add_contact

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.quicall.R
import com.example.quicall.data.FormField
import com.example.quicall.screens.components.InfoFieldsSection

@Composable
fun CreateContactPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AddContactPageViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.chooseContactImage(uri)
        }
    }

    CreateContactContent(
        modifier = modifier,
        uiState = uiState,
        photoPickerLauncher = photoPickerLauncher,
        onBackClicked = { navController.popBackStack() },
        onTextFieldValueChange = { newValue, field -> viewModel.onTextFieldValueChange(newValue, field) },
        showTextFieldError = { isTextVisible, isFieldEmpty, field -> viewModel.showTextFieldError(isTextVisible, isFieldEmpty, field) },
        onSaveContact = { viewModel.onSaveContact() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateContactContent(
    modifier: Modifier = Modifier,
    uiState: AddContactUiState,
    photoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>? = null,
    onBackClicked: () -> Unit = {},
    onTextFieldValueChange: (String, FormField) -> Unit = { _, _ -> },
    showTextFieldError: (Boolean, Boolean, FormField) -> Unit = { _, _, _ ->},
    onSaveContact: () -> Unit = {},
) {

    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT



    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Create Contact") },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.btn_icon_size))
                            .clickable{
                                onBackClicked()
                            },
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null
                    )
                }
            )
        }
    ) { paddingValues ->

        val asyncImage = rememberAsyncImagePainter(model = uiState.selectedImageUri)
        if (isPortrait) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable {
                            photoPickerLauncher?.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        )
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        painter =
                            if (uiState.selectedImageUri != null)
                                asyncImage
                            else
                                painterResource(id = R.drawable.ic_person),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.BottomEnd)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.btn_icon_size)),
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }

                InfoFieldsSection(
                    uiState = uiState,
                    onBackClicked = onBackClicked,
                    onTextFieldValueChange = onTextFieldValueChange,
                    showTextFieldError = showTextFieldError,
                    onSaveContact = onSaveContact
                )

            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .clickable {
                            photoPickerLauncher?.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        painter =
                            if (uiState.selectedImageUri != null)
                                asyncImage
                            else
                                painterResource(id = R.drawable.ic_person),
                        contentDescription = null,

                        contentScale = ContentScale.Crop,
                    )

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.BottomEnd)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.btn_icon_size)),
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                InfoFieldsSection(
                    uiState = uiState,
                    modifier = Modifier
                        .weight(1f)
                        .imePadding()
                        .verticalScroll(scrollState),
                    onBackClicked = onBackClicked,
                    onTextFieldValueChange = onTextFieldValueChange,
                    showTextFieldError = showTextFieldError,
                    onSaveContact = onSaveContact
                )

            }
        }



    }
}

@Preview
@Composable
fun CreateContactContentPreview() {
    CreateContactContent(
        modifier = Modifier.fillMaxSize(),
        uiState = AddContactUiState()
    )
}
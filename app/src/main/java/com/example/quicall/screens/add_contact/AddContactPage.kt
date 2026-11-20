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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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

    var isPhotoPickerOpen by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.chooseContactImage(uri)
        }
        isPhotoPickerOpen = false
    }

    val onChooseImage: () -> Unit = {
        if (!isPhotoPickerOpen) {
            isPhotoPickerOpen = true

            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    CreateContactContent(
        modifier = modifier,
        uiState = uiState,
        navController = navController,
        onBackClicked = { navController.popBackStack() },
        onTextFieldValueChange = { newValue, field -> viewModel.onTextFieldValueChange(newValue, field) },
        showTextFieldError = { isTextVisible, isFieldEmpty, field -> viewModel.showTextFieldError(isTextVisible, isFieldEmpty, field) },
        onSaveContact = { viewModel.onSaveContact() },
        onChooseImage = onChooseImage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateContactContent(
    modifier: Modifier = Modifier,
    uiState: AddContactUiState,
    navController: NavHostController? = null,
    onBackClicked: () -> Unit = {},
    onTextFieldValueChange: (String, FormField) -> Unit = { _, _ -> },
    showTextFieldError: (Boolean, Boolean, FormField) -> Unit = { _, _, _ ->},
    onSaveContact: () -> Boolean = { false },
    onChooseImage: () -> Unit = {}
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
                            .clickable {
                                onBackClicked()
                            },
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null
                    )
                }
            )
        }
    ) { paddingValues ->

        if (isPortrait) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
            ) {

                CreateProfilePhoto(
                    size = 150.dp,
                    uiState = uiState,
                    onChooseImage = onChooseImage
                )

                InfoFieldsSection(
                    uiState = uiState,
                    navController = navController,
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

                CreateProfilePhoto(
                    size = 200.dp,
                    uiState = uiState,
                    onChooseImage = onChooseImage
                )

                InfoFieldsSection(
                    uiState = uiState,
                    navController = navController,
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

@Composable
private fun CreateProfilePhoto(
    size: Dp,
    uiState: AddContactUiState,
    onChooseImage: () -> Unit = {},
) {
    val asyncImage = rememberAsyncImagePainter(model = uiState.selectedImageUri)
    val iconResource = painterResource(id = R.drawable.ic_person)
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable {
                onChooseImage()
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
                    iconResource,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.BottomCenter)
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
}

@Preview
@Composable
fun CreateContactContentPreview() {
    CreateContactContent(
        modifier = Modifier.fillMaxSize(),
        uiState = AddContactUiState()
    )
}
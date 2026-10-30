package com.example.quicall

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quicall.screens.add_contact.AddContactPageViewModel
import com.example.quicall.screens.add_contact.CreateContactPage
import com.example.quicall.screens.starting.StartingPage
import com.example.quicall.screens.starting.StartingPageModal
import com.example.quicall.screens.starting.StartingPageViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuiCallApp() {
    val navController = rememberNavController()
    val viewModel: StartingPageViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val executeActualCall: (String) -> Unit = { phoneNumber ->

        if (uiState.dialSheetState) {
            scope.launch {
                sheetState.hide()
                viewModel.removeDialDigits()
            }.invokeOnCompletion { viewModel.onDialShow(false) }
        }
        Toast.makeText(context, phoneNumber, Toast.LENGTH_SHORT).show()
        viewModel.makeCall(context, phoneNumber)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            val phoneNumber = viewModel.uiState.value.selectedPhoneNumber
            executeActualCall(phoneNumber)
        }
    }

    val onCallAttempt: (String) -> Unit = { phoneNumber ->
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            executeActualCall(phoneNumber)
        } else {
            permissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    val currentSelectedIndex = when {
        currentRoute == "add_contact" -> 1
        uiState.dialSheetState -> 2
        else -> 0
    }
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    NavigationSuiteScaffold(
        layoutType =
            if (currentSelectedIndex == 1) NavigationSuiteType.None
                else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(windowAdaptiveInfo),
        navigationSuiteItems = {
            menuItems.forEachIndexed { index, item ->
                item(
                    selected = index == currentSelectedIndex,
                    onClick = {
                        when (index) {
                            0, 1 -> {
                                navController.navigate(if (index == 0)"contacts" else "add_contact") {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    restoreState = true
                                    launchSingleTop = true
                                }
                            }
                            2 -> {
                                viewModel.onDialShow(true)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.size(dimensionResource(R.dimen.btn_icon_size)),
                            painter = painterResource(item.iconRes),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(stringResource(item.labelRes))
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "contacts",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("contacts") {
                StartingPage(
                    Modifier.fillMaxSize(),
                    viewModel = viewModel,
                    navController = navController,
                    onCallAttempt = onCallAttempt
                )
            }

            composable(route = "add_contact") {
                CreateContactPage(
                    Modifier.fillMaxSize(),
                    navController = navController,
                )
            }
        }
    }

    if (uiState.dialSheetState) {
        StartingPageModal(
            modifier = Modifier.fillMaxWidth(),
            sheetState = sheetState,
            onCallAttempt = onCallAttempt,
            onDialDismissed = {
                viewModel.onDialShow(false)
            },
            dialDigits = uiState.dialDigits,
            onDialDigitsChanged = {
                viewModel.onDialDigitsChanged(it)
            },
            onDialDeleteButtonPressed = {
                viewModel.onDialDeleteButtonPressed()
            },
            onDialButtonPressed = { digit ->
                viewModel.onDialButtonPressed(digit)
            },
            phoneNumberToCall = { phoneNumber ->
                viewModel.phoneNumberToCall(phoneNumber)
            }
        )
    }
}
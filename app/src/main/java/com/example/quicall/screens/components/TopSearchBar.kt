package com.example.quicall.screens.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.quicall.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier,
    queryText: String,
    onQueryChange: (String) -> Unit = {}
) {
    val searchBarState = rememberSearchBarState()
    SearchBar(
        modifier = modifier,
        state = searchBarState,
        inputField = {
            SearchBarDefaults.InputField(
                query = queryText,
                onQueryChange = {
                    onQueryChange(it)
                },
                onSearch = {},
                expanded = false,
                onExpandedChange = {},
                placeholder = { Text(stringResource(R.string.search_bar_hint)) },
                leadingIcon = {
                    //TODO add back icon
                }
            )
        }
    )
}
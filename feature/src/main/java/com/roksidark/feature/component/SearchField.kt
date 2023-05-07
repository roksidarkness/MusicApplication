package com.roksidark.feature.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.roksidark.feature.MainViewModel

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun SearchField(
    onSearchTextChanged: (String) -> Unit,
    viewModel: MainViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchText by viewModel.searchText.collectAsState(initial = "")

    OutlinedTextField(
        value = searchText,
        onValueChange = { newSearchText ->
            onSearchTextChanged(newSearchText)
            viewModel.updateSearchText(newSearchText)
        },
        label = { Text("Search") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    )

    if (searchText.isEmpty()) {
        keyboardController?.hide()
    }
}

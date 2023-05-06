package com.roksidark.feature.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class)
@Composable
fun SearchField(
    onSearchTextChanged: (String) -> Unit
) {
    val searchText = remember { mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = searchText.value,
        onValueChange = { value ->
            searchText.value = value
            onSearchTextChanged(value.text)
        },
        label = { Text("Search") },
        modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    )

    if (searchText.value.text.isEmpty()){
        keyboardController?.hide()
    }
}

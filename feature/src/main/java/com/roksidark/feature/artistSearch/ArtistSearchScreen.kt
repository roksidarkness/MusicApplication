package com.roksidark.feature.artistSearch


import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.roksidark.core.data.model.entity.artist.Artist
import com.roksidark.feature.MainViewModel
import com.roksidark.feature.component.LoadingBar
import com.roksidark.feature.component.SearchField
import com.roksidark.feature.navigation.NavigationTree
import com.roksidark.feature.util.DataState
import com.roksidark.feature.util.getTagsText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArtistSearchScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    Box {
        val isLoading by viewModel.isLoading.observeAsState(initial = false)
        val items: State<DataState<List<Artist>>> = viewModel.items.observeAsState(initial = DataState.Success(emptyList()))

        Column(modifier = Modifier.padding(8.dp)) {

            SearchField(onSearchTextChanged = { searchText ->
                viewModel.performSearch(searchText)
            }, viewModel)

            when (val dataState = items.value) {
                is DataState.Success -> {
                    val artists = dataState.data
                    ArtistList(items = artists, viewModel = viewModel) { it ->
                        navController.navigate("${NavigationTree.Details.name}/${it}") {
                            popUpTo(NavigationTree.Details.name)
                        }
                    }

                    if (isLoading) {
                        LoadingBar()
                    }
                }
                is DataState.Error -> {
                    val errorMessage = dataState.message
                    Text(text = errorMessage,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun ArtistList(
    items: List<Artist>,
    viewModel: MainViewModel,
    onItemClicked: (id: String) -> Unit = { }
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(items) { item ->
            ItemRow(item = item, viewModel = viewModel,
                onItemClicked = onItemClicked)
        }
    }
}

@Composable
fun ItemRow(
    item: Artist,
    viewModel: MainViewModel,
    onItemClicked: (id: String) -> Unit = { }
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable {
                item.id?.let {
                    onItemClicked(it)
                    viewModel.getDetails(it)
                }
            }
    ) {
        Column(modifier = Modifier
            .animateContentSize()
            .padding(16.dp)) {
            Box() {
            }
            ItemDetails(
                MaterialTheme.colorScheme.primary,
                text = item.name ?: "NO NAME",
                null
            )
            item.area?.name?.let{
                ItemDetails(
                    Color.Black,
                    text = it,
                    null
                )
            }
            item.tags?.let {
                ItemDetails(
                    MaterialTheme.colorScheme.secondary,
                    text = getTagsText(it),
                    FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun ItemDetails(
    color: Color,
    text: String,
    fontStyle: FontStyle?
) {
    Row() {
        Text(
            text = "$text",
            fontStyle = fontStyle ?: FontStyle.Normal,
            color = color,
            modifier = Modifier
                .padding(
                    start = 4.dp,
                    end = 4.dp,
                    top = 4.dp,
                    bottom = 6.dp
                ),
            fontSize = 16.sp
        )
    }
}


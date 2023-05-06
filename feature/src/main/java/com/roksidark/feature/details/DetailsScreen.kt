package com.roksidark.feature.details


import android.util.Log
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
import com.roksidark.core.data.model.entity.album.Album
import com.roksidark.core.data.model.entity.artist.Artist
import com.roksidark.core.util.Constant.TAG
import com.roksidark.feature.MainViewModel
import com.roksidark.feature.artistSearch.ArtistList
import com.roksidark.feature.artistSearch.ItemRow
import com.roksidark.feature.component.LoadingBar
import com.roksidark.feature.component.SearchField
import com.roksidark.feature.navigation.NavigationTree
import com.roksidark.feature.util.DataState
import com.roksidark.feature.util.getTagsText

@Composable
fun DetailsScreen(
    selectedItem: String,
    viewModel: MainViewModel
) {

    val isLoading by viewModel.isLoading.observeAsState(initial = true)

    val items: State<DataState<List<Album?>>> = viewModel.itemAlbums.observeAsState(initial = DataState.Success(emptyList()))

    when (val dataState = items.value) {
        is DataState.Success -> {
            val albums = dataState.data
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.padding(8.dp)) {

                    AlbumList(items = albums, viewModel = viewModel)

                    if (isLoading) {
                        LoadingBar()
                    }
                }
            }
        }
        is DataState.Error -> {
            val errorMessage = dataState.message
            Text(text = errorMessage, modifier = Modifier.padding(8.dp))
        }
    }
}


@Composable
fun AlbumList(
    items: List<Album?>,
    viewModel: MainViewModel
) {
    if (items.isNotEmpty()) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {

            items(items) { item ->
                ItemRow(item = item, viewModel = viewModel)
            }
    }
    }
    else {
        EmptyList()
    }
}

@Composable
fun ItemRow(
    item: Album?,
    viewModel: MainViewModel,
    onItemClicked: (id: String) -> Unit = { }
) {
    item?.let{
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .animateContentSize()
                    .padding(16.dp)
            ) {
                Box() {
                }
                ItemDetails(
                    MaterialTheme.colorScheme.primary,
                    text = item.title ?: "NO TITLE",
                    null
                )

                item.date?.let {
                    ItemDetails(
                        Color.Black,
                        text = it,
                        null
                    )
                }
                item.status?.let {
                    ItemDetails(
                        MaterialTheme.colorScheme.secondary,
                        text = it,
                        FontStyle.Italic
                    )
                }

            }
        }
    }
}
@Composable
fun EmptyList(){
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .padding(16.dp)
        ) {
            Box() {
                Text(text = "No albums",
                    modifier = Modifier
                        .padding(
                            start = 4.dp,
                            end = 4.dp,
                            top = 4.dp,
                            bottom = 6.dp
                        ),
                    fontSize = 16.sp)
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

package com.roksidark.feature.details


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roksidark.core.data.model.entity.album.Album
import com.roksidark.core.data.model.entity.artist.Artist
import com.roksidark.feature.MainViewModel
import com.roksidark.feature.component.ItemDetails
import com.roksidark.feature.component.LoadingBar
import com.roksidark.feature.util.DataState
import com.roksidark.feature.util.getTagsText

@Composable
fun DetailsScreen(
    selectedItem: String,
    viewModel: MainViewModel
) {

    val isLoading by viewModel.isLoading.observeAsState(initial = true)

    val items: State<DataState<List<Album?>>> =
        viewModel.itemAlbums.observeAsState(initial = DataState.Success(emptyList()))

    val artists by viewModel.items.observeAsState()

    val item = findArtistById(selectedItem, artists)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(8.dp)) {
            item?.let { it ->
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            ItemDetails(
                                MaterialTheme.colorScheme.primary,
                                text = item.name ?: "NO NAME",
                                null
                            )
                            item.area?.name?.let {
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

                    Text(
                        text = "Albums: ",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                top = 16.dp,
                                end = 16.dp,
                                bottom = 0.dp
                            )
                    )
                }
            }

            when (val dataState = items.value) {
                is DataState.Success -> {
                    val albums = dataState.data

                    Column(
                        modifier = Modifier.padding(
                            start = 8.dp,
                            top = 0.dp,
                            end = 8.dp,
                            bottom = 0.dp
                        )
                    ) {

                        AlbumList(items = albums, viewModel = viewModel)

                        if (isLoading) {
                            LoadingBar()
                        }
                    }

                }

                is DataState.Error -> {
                    val errorMessage = dataState.message
                    Text(
                        text = errorMessage,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
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
    } else {
        EmptyList()
    }
}

@Composable
fun ItemRow(
    item: Album?,
    viewModel: MainViewModel,
    onItemClicked: (id: String) -> Unit = { }
) {
    item?.let {
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
fun EmptyList() {
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
                Text(
                    text = "No albums",
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
    }
}

fun findArtistById(artistId: String, artists: DataState<List<Artist>>?): Artist? {
    if (artists is DataState.Success) {
        val artistList = artists.data
        return artistList.find { artist -> artist.id == artistId }
    }
    return null
}

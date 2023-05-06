package com.roksidark.feature.details


import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.roksidark.core.data.model.entity.Artist
import com.roksidark.feature.MainViewModel
import com.roksidark.feature.component.LoadingBar

@Composable
fun DetailsScreen(
    selectedItem: String,
    viewModel: MainViewModel
) {
    val item by viewModel.itemAlbums.observeAsState()

    item?.let{
            item ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                  //  elevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextDetails(
                          //  label = textResource(id = R.string.label_start_date).toString(),
                            label = "",
                            text = item.get(0)?.title ?: "NO TITLE",
                            null
                        )

                    }
                }
            }
        }
    }

}

@Composable
fun TextDetails(
    label: String,
    text: String,
    color: String?
) {
    Row() {
        Text(
            text = "$label",
            color = MaterialTheme.colorScheme.inverseSurface,
            modifier = Modifier
                .padding(
                    start = 6.dp,
                    end = 4.dp,
                    top = 4.dp,
                    bottom = 6.dp
                ),
            fontSize = 16.sp
        )
        Text(
            text = "$text",
            color = MaterialTheme.colorScheme.primary,
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

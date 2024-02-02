package sources.anime

import AsyncImage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import loadImageBitmap

@Composable
fun singleAnimeView(anime: Anime, backOut: () -> Unit) {
    Column {
        Button(modifier = Modifier.padding(8.dp), onClick = backOut) {
            Text("←")
        }
        Row {
            AsyncImage(
                load = { loadImageBitmap(anime.coverImage.toString()) },
                painterFor = { remember { BitmapPainter(it) } },
                contentDescription = anime.title.default,
                modifier = Modifier.width(200.dp)
            )
            Text(anime.description)
        }
    }
}
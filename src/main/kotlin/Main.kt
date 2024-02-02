import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sources.anime.Anime
import sources.anime.AnimeSource
import sources.anime.anilist.AnilistSource
import java.io.ByteArrayInputStream
import java.io.IOException

val animeSource: AnimeSource = AnilistSource()

@OptIn(ExperimentalLayoutApi::class)
fun main() = singleWindowApplication {
    var animes by remember { mutableStateOf(emptyList<Anime>()) }
    var selectedAnime: Anime? by remember { mutableStateOf(null) }
    MaterialTheme {
        Column {
            Row {
                Button(onClick = { selectedAnime = null }) {
                    Text("←")
                }
                SearchBar(onQuery = {
                    animes = it
                }, clearAnime = { animes = emptyList() })
            }
            if (selectedAnime == null) {
                FlowRow(modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState())) {
                    for (anime in animes) {
                        AnimeCard(anime) { selectedAnime = anime }
                    }
                }
            } else {
                Text(selectedAnime!!.title.default)
            }
        }
    }
}

@Composable
fun SearchBar(onQuery: (List<Anime>) -> Unit, clearAnime: () -> Unit) {
    var knop by remember { mutableStateOf("Search") }
    var queryTitle by remember { mutableStateOf("naruto") }
    Row {
        OutlinedTextField(
            value = queryTitle,
            onValueChange = { queryTitle = it },
            maxLines = 1,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(onClick = {
            knop = "Searching"
            CoroutineScope(Dispatchers.IO).launch {
                clearAnime()
                onQuery(animeSource.search(queryTitle))
                knop = "searched :)"
            }
        }) {
            Text(knop)
        }
    }
}

@Composable
fun AnimeCard(anime: Anime, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(8.dp).width(200.dp).clickable(onClick = onClick)) {
        AsyncImage(
            load = { loadImageBitmap(anime.coverImage.toString()) },
            painterFor = { remember { BitmapPainter(it) } },
            contentDescription = anime.title.default,
            modifier = Modifier.width(200.dp)
        )
        Text(anime.title.default, softWrap = true)
    }
}

@Composable
fun <T> AsyncImage(
    load: suspend () -> T,
    painterFor: @Composable (T) -> Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val image: T? by produceState<T?>(null) {
        value = withContext(Dispatchers.IO) {
            try {
                load()
            } catch (e: IOException) {
                // instead of printing to console, you can also write this to log,
                // or show some error placeholder
                e.printStackTrace()
                null
            }
        }
    }

    if (image != null) {
        Image(
            painter = painterFor(image!!),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

suspend fun loadImageBitmap(url: String): ImageBitmap =
    urlStream(url).use(::loadImageBitmap)

private suspend fun urlStream(url: String) = HttpClient(CIO).use {
    ByteArrayInputStream(it.get(url).readBytes())
}
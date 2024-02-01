import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.wtas.fileLocator.graphql.anilist.SearchQuery
import me.wtas.fileLocator.graphql.anilist.type.MediaType
import sources.anime.Anime
import sources.anime.anilist.toInternal
import java.io.ByteArrayInputStream
import java.io.IOException

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://graphql.anilist.co")
    .build()
@OptIn(ExperimentalLayoutApi::class)
fun main() = singleWindowApplication {
    var title by remember { mutableStateOf("naruto") }
    var animes by remember { mutableStateOf(emptyList<Anime>()) }
    var knop by remember { mutableStateOf("Search") }
    MaterialTheme {
    Column {
        Row {
            OutlinedTextField(value = title, onValueChange = { title = it }, maxLines = 1, modifier = Modifier.padding(bottom = 8.dp))
            Button(onClick = {
                animes = emptyList()
                knop = "Searching"
                CoroutineScope(Dispatchers.IO).launch {
                    println(title)
                    animes = apolloClient.query(
                        SearchQuery(type = Optional.present(MediaType.ANIME), search = Optional.present(title))
                    ).execute().data?.Page?.media?.mapNotNull { it?.toInternal() } ?: emptyList()
                    knop = "searched :)"
                }
            }) {
                Text(knop)
            }
        }

        FlowRow(modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState())) {
            for (anime in animes) {
                Column(modifier = Modifier.padding(8.dp).width(200.dp)) {
                     AsyncImage(load = { loadImageBitmap(anime.coverImage.toString())},
                                painterFor = { remember { BitmapPainter(it) } },
                                contentDescription = anime.title.default,
                                modifier = Modifier.width(200.dp))
                    Text(anime.title.default, softWrap = true)
                }

            }
        }
    }}
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
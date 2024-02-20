import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import io.kamel.core.Resource
import io.kamel.image.KamelImageBox
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sources.anime.Anime
import sources.anime.AnimeSource
import sources.anime.SingleAnimeView
import sources.anime.anilist.AnilistSource

val animeSource: AnimeSource = AnilistSource()

@OptIn(ExperimentalLayoutApi::class)
fun main() = singleWindowApplication {
    var animes by remember { mutableStateOf(emptyList<Anime>()) }
    var selectedAnime: Anime? by remember { mutableStateOf(null) }
    MaterialTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                if (selectedAnime != null) {
                Column {
                    IconButton(modifier = Modifier.padding(8.dp), onClick = { selectedAnime = null }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }

//                }
                Column {
                    SearchBar(onQuery = {
                        animes = it
                    })
                }
                Column {

                }
            }
            if (selectedAnime == null) {
                FlowRow(modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState())) {
                    for (anime in animes) {
                        AnimeCard(anime) { selectedAnime = anime }
                    }
                }
            } else {
                SingleAnimeView(selectedAnime!!, backOut = { selectedAnime = null })
            }
        }
    }
}

@Composable
fun SearchBar(onQuery: (List<Anime>) -> Unit) {
    var knop by remember { mutableStateOf("Search") }
    var queryTitle by remember { mutableStateOf("naruto") }
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = queryTitle,
            onValueChange = { queryTitle = it },
            maxLines = 1
        )
        Button(modifier = Modifier.padding(start = 8.dp), onClick = {
            knop = "Searching"
            CoroutineScope(Dispatchers.IO).launch {
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
        KamelImage(
            resource = asyncPainterResource(anime.coverImage),
            contentDescription = anime.title.default,
            modifier = Modifier.width(200.dp)
        )
        Text(anime.title.default, softWrap = true)
    }
}

// https://github.com/Kamel-Media/Kamel/issues/94
@Composable
public fun KamelImage(
    resource: Resource<Painter>,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    onLoading: @Composable (BoxScope.(Float) -> Unit)? = null,
    onFailure: @Composable (BoxScope.(Throwable) -> Unit)? = null,
    contentAlignment: Alignment = Alignment.Center,
    animationSpec: FiniteAnimationSpec<Float>? = null,
) {
    val onSuccess: @Composable (BoxScope.(Painter) -> Unit) = { painter ->
        Image(
            painter,
            contentDescription,
            Modifier,
            alignment,
            contentScale,
            alpha,
            colorFilter
        )
    }
    KamelImageBox(
        resource,
        modifier,
        contentAlignment,
        animationSpec,
        onLoading,
        onFailure,
        onSuccess,
    )
}

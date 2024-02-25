import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import io.kamel.core.Resource
import io.kamel.image.KamelImageBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.extism.sdk.manifest.Manifest
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import org.koin.dsl.module
import plugin.PluginDriver
import sources.anime.SingleMediaView

object HomeTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Home"
            val icon = rememberVectorPainter(Icons.Default.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        // ...
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun App(manifest: Manifest) {
    startKoin {
        modules(module {
            single<PluginDriver> {
                PluginDriver().also {
                    it.addPlugin(manifest)
                }
            }
        })
    }


    var medias by remember { mutableStateOf(emptyList<Content>()) }
    var selectedMedia: Content? by remember { mutableStateOf(null) }
    MaterialTheme {
        TabNavigator(HomeTab) {
            Scaffold(
                content = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                IconButton(modifier = Modifier.padding(8.dp), onClick = { selectedMedia = null }) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                                }
                            }

                            Column {
                                SearchBar(onQuery = {
                                    medias = it
                                })
                            }
                            Column {

                            }
                        }
                        if (selectedMedia == null) {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState())
                            ) {
                                for (anime in medias) {
                                    AnimeCard(anime) { selectedMedia = anime }
                                }
                            }
                        } else {
                            SingleMediaView(selectedMedia!!, backOut = { selectedMedia = null })
                        }
                    }
                },
                bottomBar = {


                }
            )
        }
    }
}

@Composable
fun SearchBar(onQuery: (List<Content>) -> Unit) {
    val pluginDriver = get().get<PluginDriver>()
    val p = pluginDriver.getPlugins().first()

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
                onQuery(p.search(queryTitle))
                knop = "searched :)"
            }
        }) {
            Text(knop)
        }
    }
}

@Composable
fun AnimeCard(media: Content, onClick: () -> Unit) {
//    Column(modifier = Modifier.padding(8.dp).width(200.dp).clickable(onClick = onClick)) {
//        if (media.coverImage != null) {
//            KamelImage(
//                resource = asyncPainterResource(media.coverImage!!),
//                contentDescription = media.defaultTitle,
//                modifier = Modifier.width(200.dp)
//            )
//        } else {
//            Box(modifier = Modifier.width(200.dp)) {
//                Text(media.defaultTitle)
//            }
//        }
//        Text(media.defaultTitle, softWrap = true)
//    }
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

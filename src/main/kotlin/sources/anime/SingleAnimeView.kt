package sources.anime

import androidx.compose.foundation.layout.Box
import KamelImage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.kamel.image.asyncPainterResource
import sources.Media

@Composable
fun SingleMediaView(media: Media, backOut: () -> Unit) {
    val leftAreaWidth = 200.dp
    Column {
        media.bannerImage?.let {
            KamelImage(
                resource = asyncPainterResource(it),
                contentDescription = media.defaultTitle + " Banner",
            )
        }
        Row {
            if (media.coverImage != null) {
                KamelImage(
                    resource = asyncPainterResource(media.coverImage!!),
                    contentDescription = media.defaultTitle,
                    modifier = Modifier.width(leftAreaWidth)
                )
            } else {
                Box(modifier = Modifier.width(leftAreaWidth)) {
                    Text(media.defaultTitle)
                }
            }
            media.description?.let { Text(it) }

        }
        Row {
            InformationList(media, Modifier.width(leftAreaWidth))
            ContentList(media)
        }
    }
}

@Composable
fun InformationList(media: Media, modifier: Modifier) {
    Column(modifier = modifier) {
        for (informationTag in media.informationTags) {
            InformationTagList(informationTag.key, informationTag.values)
        }
//        anime.run {
//            "Next Episode" tag nextAiringSchedule?.run { "Episode $episode: $time" }
//            "Format" tag format
//            "Episodes" tag "$duration minutes"
//            "Episode Duration" tag duration.toString()
//            "Status" tag status?.toString()
//            "Start Date" tag startDate?.toString()
//            "End Date" tag endDate?.toString()
//            "Season" tag startDate?.run { "$season $year" }
//            "Average Score" tag averageScore?.toString()
//            "Studios" tagList studios
//            "Genres" tagList genres
//        }
    }
}

@Composable
infix fun String.tag(value: String?) {
    if (value != null) InformationTag(this, value)
}

@Composable
infix fun String.tagList(value: List<String>) {
    InformationTagList(this, value)
}

@Composable
fun InformationTag(type: String, content: String) {
    Column {
        Text(text = type, fontWeight = FontWeight.Bold)
        Text(text = content, fontStyle = FontStyle.Italic )
    }
}

@Composable
fun InformationTagList(type: String, content: List<String>) {
    Column {
        Text(text = type, fontWeight = FontWeight.Bold)
        for (item in content) {
            Text(text = item, fontStyle = FontStyle.Italic)
        }
    }
}

@Composable
fun ContentList(media: Media) {
    LazyColumn {
        // Add a single item
        item {
            Text(text = "First item")
        }

        // Add 5 items
        items(100) { index ->
            Text(text = "Item: $index")
        }

        // Add another single item
        item {
            Text(text = "Last item")
        }

    }
}

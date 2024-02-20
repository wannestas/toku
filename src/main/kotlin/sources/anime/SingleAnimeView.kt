package sources.anime

import KamelImage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.kamel.image.asyncPainterResource

@Composable
fun SingleAnimeView(anime: Anime, backOut: () -> Unit) {
    val leftAreaWidth = 200.dp
    Column {
        anime.bannerImage?.let {
            KamelImage(
                resource = asyncPainterResource(it),
                contentDescription = anime.title.default + " Banner",
            )
        }
        Row {
            KamelImage(
                resource = asyncPainterResource(anime.coverImage),
                contentDescription = anime.title.default,
                modifier = Modifier.width(leftAreaWidth)
            )
            Text(anime.description)
        }
        Row {
            InformationList(anime, Modifier.width(leftAreaWidth))
            EpisodeList(anime)
        }
    }
}

@Composable
fun InformationList(anime: Anime, modifier: Modifier) {
    Column(modifier = modifier) {
        anime.run {
            "Next Episode" tag nextAiringSchedule?.run { "Episode $episode: $time" }
            "Format" tag format
            "Episodes" tag "$duration minutes"
            "Episode Duration" tag duration.toString()
            "Status" tag status?.toString()
            "Start Date" tag startDate?.toString()
            "End Date" tag endDate?.toString()
            "Season" tag startDate?.run { "$season $year" }
            "Average Score" tag averageScore?.toString()
            "Studios" tagList studios
            "Genres" tagList genres
        }
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
fun EpisodeList(anime: Anime) {
    Column {
        Row {
            Text(text = "Episodes will be here")
        }
    }
}

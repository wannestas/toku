package sources.anime.anilist

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.wtas.toku.graphql.anilist.SearchQuery
import me.wtas.toku.graphql.anilist.type.MediaSeason
import me.wtas.toku.graphql.anilist.type.MediaStatus
import sources.anime.*
import java.net.URI
import kotlin.time.Duration.Companion.seconds

const val MISSING_IMG_URI = "TODO"

fun MediaSeason.toInternal() = when (this) {
    MediaSeason.WINTER -> Season.WINTER
    MediaSeason.SPRING -> Season.SPRING
    MediaSeason.SUMMER -> Season.SUMMER
    MediaSeason.FALL -> Season.FALL
    MediaSeason.UNKNOWN__ -> Season.UNKNOWN
}

fun MediaStatus.toInternal() = when (this) {
    MediaStatus.FINISHED -> Status.FINISHED
    MediaStatus.RELEASING -> Status.RELEASING
    MediaStatus.NOT_YET_RELEASED -> Status.NOT_YET_RELEASED
    MediaStatus.CANCELLED -> Status.CANCELLED
    MediaStatus.HIATUS -> Status.HIATUS
    MediaStatus.UNKNOWN__ -> Status.UNKNOWN
}

fun SearchQuery.NextAiringEpisode.toInternal() = NextAiringEpisode(
    time = (Clock.System.now() + timeUntilAiring.seconds).toLocalDateTime(TimeZone.currentSystemDefault()),
    episode = episode,
)

fun SearchQuery.Studios.toInternal() = this.edges?.mapNotNull { it?.node?.name } ?: emptyList()

fun SearchQuery.Medium.toInternal() = this.let {
    object : Anime {
        override val id: String = it.id.toString()
        override val title: Title = Title(
            it.title?.romaji,
            it.title?.native,
            it.title?.english,
            it.synonyms?.filterNotNull() ?: emptyList()
        )
        override val coverImage: URI = URI(it.coverImage?.extraLarge ?: MISSING_IMG_URI)
        override val bannerImage: URI? = it.bannerImage?.let { img -> URI(img) }
        override val startDate: LocalDate? = it.startDate?.run { if (year != null && month != null && day != null) LocalDate(year, month, day) else null }
        override val endDate: LocalDate? = it.endDate?.run { if (year != null && month != null && day != null) LocalDate(year, month, day) else null }
        override val season: Season = it.season?.toInternal() ?: Season.UNKNOWN
        override val description: String = it.description ?: "???"
        override val episodes: Int = it.episodes ?: -1
        override val duration: Int = it.duration ?: -1
        override val genres: List<String> = it.genres?.filterNotNull() ?: emptyList()
        override val isAdult: Boolean = it.isAdult ?: false
        override val averageScore: Int? = it.averageScore
        override val popularity: Int? = it.popularity
        override val format: String? = it.format?.toString()
        override val status: Status? = it.status?.toInternal()
        override val nextAiringSchedule: NextAiringEpisode? = it.nextAiringEpisode?.toInternal()
        override val studios: List<String> = it.studios?.toInternal() ?: emptyList()
        override fun toString(): String {
            return "Chocomous Anime(id='$id', title=$title, coverImage=$coverImage, bannerImage=$bannerImage, startDate=$startDate, endDate=$endDate, season=$season, description='$description', episodes=$episodes, duration=$duration, genres=$genres, isAdult=$isAdult, averageScore=$averageScore, popularity=$popularity, format=$format, status=$status, nextAiringSchedule=$nextAiringSchedule, studios=$studios)"
        }


    }
}

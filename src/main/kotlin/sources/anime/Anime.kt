package sources.anime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.net.URI

interface Anime {
    val id: String
    val title: Title
    val coverImage: URI
    val startDate: LocalDate?
    val endDate: LocalDate?
    val bannerImage: URI?
    val season: Season
    val description: String
    val format: String?
    val status: Status?
    val episodes: Int
    val duration: Int
    val genres: List<String>
    val isAdult: Boolean
    val averageScore: Int?
    val popularity: Int?
    val nextAiringSchedule: NextAiringEpisode?
    val studios: List<String>
}

data class Title(val romaji: String?, val native: String?, val english: String?, val synonyms: List<String>) {
    val default = english ?: romaji ?: native ?: synonyms.firstOrNull() ?: "?????"
}

data class NextAiringEpisode(val time: LocalDateTime, val episode: Int)
enum class Season {
    WINTER, SPRING, SUMMER, FALL, UNKNOWN
}

enum class Status {
    FINISHED, RELEASING, NOT_YET_RELEASED, CANCELLED, HIATUS, UNKNOWN
}

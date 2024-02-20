import sources.anime.Anime
import java.io.File

interface FileSource {

    fun getConfigOptions(): List<ConfigOption>

    fun setConfigOptions(config: List<ConfigOption>)

    fun getEpisodes(anime: Anime): List<Downloadable>

    fun downloadEpisode(episode: Downloadable, location: File)
}

interface Downloadable {}

data class ConfigOption(val id: String, val name: String, val description: String, val type: String, val constraints: List<String>)
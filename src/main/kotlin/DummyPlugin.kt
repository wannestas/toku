import kotlinx.serialization.json.Json
import sources.Media
import sources.anime.Anime
import java.io.File

class DummyPlugin : Plugin {
    override fun getPluginDefinition(): PluginDefinition {
        return PluginDefinition("5", "5", "5", "5", emptyList())
    }

    override fun getConfigOptions(): List<ConfigOption> {
        return emptyList()
    }

    override fun setConfigOption(optionId: String, value: Json) {

    }

    override fun getContents(content: Content): List<Content> {
        return emptyList()
    }


    override fun search(title: String): List<Media> {
        return emptyList()
    }
}
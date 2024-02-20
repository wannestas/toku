import kotlinx.serialization.json.Json
import sources.Media

interface Plugin {
    fun getPluginDefinition(): PluginDefinition

    fun getConfigOptions(): List<ConfigOption>

    fun setConfigOption(optionId: String, value: Json)

    fun getContents(content: Content): List<Content>

    fun search(title: String): List<Media>
}

interface Content {
    val id: String
    val name: String
    val featureList: List<String>
    val description: String?
    val informationList: Json
}

data class PluginDefinition(val id: String, val name: String, val description: String, val version: String, val features: List<String>)
data class ConfigOption(val id: String, val name: String, val description: String, val type: String, val constraints: List<String>)
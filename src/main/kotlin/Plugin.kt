import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

interface Plugin {
    fun getPluginDefinition(): PluginDefinition

    fun getConfigOptions(): List<ConfigOption>

    fun setConfigOption(optionId: String, value: JsonElement)

    fun getContents(id: String): List<Content>

    fun search(query: String): List<Content>
}

@Serializable
data class Content(
    val id: String,
    val name: String,
    val featureList: List<String>,
    val description: String?,
    val informationList: JsonElement
)

@Serializable
data class PluginDefinition(
    val id: String,
    val name: String,
    val description: String,
    val version: String,
    val features: List<String>
)

@Serializable
data class ConfigOption(
    val id: String,
    val name: String,
    val description: String,
    val type: String,
    val constraints: List<String>
)

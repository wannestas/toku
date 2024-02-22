package plugin

import ConfigOption
import Content
import Plugin
import PluginDefinition
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.extism.sdk.manifest.Manifest
import org.extism.sdk.wasm.WasmSourceResolver
import java.nio.file.Path

class ExtismPlugin(private val binary: org.extism.sdk.Plugin) : Plugin {
    private val features: MutableList<Any> = mutableListOf()

    private inline fun <reified R, reified I> call(fn: String, input: I? = null): R {
        val inputString = input?.let { Json.encodeToString(it) } ?: ""
        val ret = this.binary.call(fn, inputString)
        return Json.decodeFromString<R>(ret)
    }

    override fun getPluginDefinition() = call<PluginDefinition, Unit>("get_plugin_definition")

    override fun getConfigOptions() = call<List<ConfigOption>, Unit>("get_config_options")

    override fun setConfigOption(optionId: String, value: JsonElement) {
        @Serializable
        data class Input(val optionId: String, val value: JsonElement)
        return call<Unit, Input>("set_config_option", Input(optionId, value))
    }

    override fun getContents(id: String) = call<List<Content>, Unit>("get_contents")

    override fun search(query: String) = call<List<Content>, Unit>("search")

    fun getFeatures() = this.features.toList()

    fun addFeatureImplementor(feature: Any) {
        // todo: check there is no existing implementor of this type
        this.features.add(feature)
    }

    inline fun <reified T : Any> getFeatureImplementation(): T? {
        return getFeatures().filterIsInstance<T>().firstOrNull()
    }

    companion object {
        fun fromManifest(manifest: Manifest): ExtismPlugin {
            return ExtismPlugin(org.extism.sdk.Plugin(manifest, false, null))
        }

        fun fromPath(path: Path): ExtismPlugin {
            val resolver = WasmSourceResolver()
            return fromManifest(Manifest(resolver.resolve(path)))
        }
    }
}


package plugin

import Plugin
import org.extism.sdk.manifest.Manifest
import java.nio.file.Path

class PluginDriver {

    private val plugins: MutableList<Plugin> = mutableListOf()

    fun addPlugin(path: Path) {
        this.plugins.add(ExtismPlugin.fromPath(path))
    }

    fun addPlugin(manifest: Manifest) {
        this.plugins.add(ExtismPlugin.fromManifest(manifest))
    }

    fun getPlugins() = plugins.toList()

}

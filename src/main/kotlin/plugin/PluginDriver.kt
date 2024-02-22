package plugin

import Plugin
import java.nio.file.Path

class PluginDriver {

    private val plugins: MutableList<Plugin> = mutableListOf()

    fun addPlugin(path: Path) {
        this.plugins.add(ExtismPlugin.fromPath(path))
    }

    fun getPlugins() = plugins.toList()

}

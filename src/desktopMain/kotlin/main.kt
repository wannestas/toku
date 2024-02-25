import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.extism.sdk.manifest.Manifest
import org.extism.sdk.wasm.ByteArrayWasmSource
import org.extism.sdk.wasm.PathWasmSource

fun main() = application {
    val mani = Manifest(PathWasmSource("hi", "plugins/sample/target/wasm32-unknown-unknown/debug/sample_plugin.wasm", null))

    Window(onCloseRequest = ::exitApplication, title = "Toku") {
        App(mani)
    }
}
//
//@Preview
//@Composable
//fun AppDesktopPreview() {
//    App()
//}
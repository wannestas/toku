package me.wtas.toku

import App
import android.content.res.AssetManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.extism.sdk.manifest.Manifest
import org.extism.sdk.wasm.ByteArrayWasmSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mani = Manifest(ByteArrayWasmSource("hi", applicationContext.assets.open("sample_plugin.wasm").readBytes(), null))
        setContent {
            App(mani)
        }
    }
}


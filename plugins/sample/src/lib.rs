use extism_pdk::{FnResult, plugin_fn};

#[plugin_fn]
pub fn greet(name: String) -> FnResult<String> {
    Ok(format!("Hello, {}!\n", name))
}

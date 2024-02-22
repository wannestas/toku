use extism_pdk::{FnResult, info, Json, plugin_fn};
use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize)]
pub struct PluginDefinition {
    id: &'static str,
    name: &'static str,
    description: &'static str,
    version: &'static str,
    features: Vec<&'static str>
}

#[derive(Serialize, Deserialize)]
pub struct ConfigOption {
    id: String,
    name: String,
    description: String,
    r#type: String,
    constraints: Vec<String>
}

const DEF: PluginDefinition = PluginDefinition {
    id: "sample",
    name: "Sample plugin",
    description: "A plugin sample.",
    version: "0.0.1",
    features: vec![],
};

#[plugin_fn]
pub fn get_plugin_definition() -> FnResult<Json<PluginDefinition>> {
    Ok(Json(DEF))
}

#[plugin_fn]
pub fn get_config_options() -> FnResult<Json<Vec<ConfigOption>>> {
    Ok(Json(vec![]))
}

#[plugin_fn]
pub fn set_config_option(Json((option_id, value)): Json<(String, serde_json::Value)>) -> FnResult<()> {
    info!("set {option_id} to {value}");

    Ok(())
}

#[plugin_fn]
pub fn get_contents(_id: String) -> FnResult<Json<Vec<()>>> {
    Ok(Json(vec![]))
}

#[plugin_fn]
pub fn search(_query: String) -> FnResult<Json<Vec<()>>>  {
    Ok(Json(vec![]))
}

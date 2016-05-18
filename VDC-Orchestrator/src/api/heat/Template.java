package api.heat;

import com.google.gson.JsonObject;

public class Template {

	private String heat_template_version;
	private String description;
	private JsonObject resources;
	
	public Template(String heat_template_version, String description, JsonObject resources){
		this.setHeat_template_version(heat_template_version);
		this.setDescription(description);
		this.setResources(resources);
	}

	public String getHeat_template_version() {
		return heat_template_version;
	}

	public void setHeat_template_version(String heat_template_version) {
		this.heat_template_version = heat_template_version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JsonObject getResources() {
		return resources;
	}

	public void setResources(JsonObject resources) {
		this.resources = resources;
	}
}

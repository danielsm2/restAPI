package api.heat;

import com.google.gson.JsonObject;

public class Template {

	private String heat_template_version;
	private String description;
	private JsonObject resources;
	
	public Template(String heat_template_version, String description, JsonObject resources){
		this.heat_template_version = heat_template_version;
		this.description = description;
		this.resources = resources;
	}
}

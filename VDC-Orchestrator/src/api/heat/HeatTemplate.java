package api.heat;

import com.google.gson.JsonObject;

public class HeatTemplate {

	private String stack_name;
	private Template template;
	
	public HeatTemplate(String stack_name,Template template){
		this.stack_name = stack_name;
		this.template = template;
	}
}

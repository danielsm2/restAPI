package api.heat;

public class HeatTemplate {

	private String stack_name;
	private Template template;
	
	public HeatTemplate(String stack_name,Template template){
		this.setStack_name(stack_name);
		this.setTemplate(template);
	}

	public String getStack_name() {
		return stack_name;
	}

	public void setStack_name(String stack_name) {
		this.stack_name = stack_name;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}
}

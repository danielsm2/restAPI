package tenant;

public class Tenant {

	private String description;
	private String enabled;
	private String id;
	private String name;
	
	public Tenant(String description, String enabled, String id, String name){
		this.description = description;
		this.enabled = enabled;
		this.id = id;
		this.name = name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getEnabled(){
		return this.enabled;
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	@Override
	public String toString(){
		return "Tenant [Description:" + description + "Enabled:" + enabled + "id:" + id + "name:" + name + "]";
	}
}
package api.heat;

public class Element {

	private String type;
	private Properties properties;
	
	public Element(String type, Properties properties){
		this.setType(type);
		this.setProperties(properties);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}

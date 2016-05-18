package api.heat;

public class Networks {

	private String port;
	
	public Networks(String port){
		this.setPort(port);
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
}

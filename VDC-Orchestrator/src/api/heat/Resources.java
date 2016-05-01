package api.heat;

import java.util.List;

public class Resources {
	private Element network;
	private Element subnet;
	private List<Element> hosts;
	
	public Resources(Element network, Element subnet, List<Element> hosts){
		this.network = network;
		this.subnet = subnet;
		this.hosts = hosts;
	}
}

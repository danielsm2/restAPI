package topology;

import java.util.ArrayList;
import java.util.List;

import api.nova.Host;

public class Topology {

	List<Host> hosts = new ArrayList<Host>();
	List<Switch> switchs = new ArrayList<Switch>();
	
	public void addHost(Host host){
		hosts.add(host);
	}
	
	public void addSwitch(Switch s){
		switchs.add(s);
	}
}

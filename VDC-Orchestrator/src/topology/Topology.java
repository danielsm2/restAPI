package topology;

import java.util.ArrayList;
import java.util.List;

import api.nova.Host;

public class Topology {

	List<Host> hosts = new ArrayList<Host>();
	List<Switch> switches = new ArrayList<Switch>();
	List<Link> links = new ArrayList<Link>();
	
	public void addHost(Host host){
		hosts.add(host);
	}
	
	public boolean containsHost(String id){
		boolean found = false;
		for(Host aux : hosts){
			found = id == aux.getMac();
			if(found)
				break;
		}
		return found;
	}
	
	public void addSwitch(Switch s){
		switches.add(s);
	}
	
	public void addLink(String src, Integer it, String dest, Integer it2){
		if(hosts.get(it).getId().equals(src) && switches.get(it2).getId().equals(dest)){
			links.add(new Link(hosts.get(it),switches.get(it2)));
			hosts.get(it).addLink();
			switches.get(it2).addLink();
		}
		else if(hosts.get(it2).getId().equals(dest) && switches.get(it).getId().equals(src)){
			links.add(new Link(hosts.get(it2),switches.get(it)));
			hosts.get(it2).addLink();
			switches.get(it).addLink();
		}
		else{
			links.add(new Link(switches.get(it),switches.get(it2)));
			switches.get(it).addLink();
			switches.get(it2).addLink();
		}
	}
	
	public void clearTopology(){
		for(Switch auxS : switches){
			if(auxS.getnLink() == 2){
				for(Link auxL : links){
					if(auxL.getDestId().equals(auxS.getId()) || auxL.getSrcId().equals(auxS.getId()))
						links.remove(auxL);
				}
			}
		}
	}
}

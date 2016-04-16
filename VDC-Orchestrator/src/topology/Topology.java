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
		int nHost = hosts.size();
		int nSwitch = switches.size();
		if(nHost > it && nSwitch > it2 && hosts.get(it).getId().equals(src) && switches.get(it2).getId().equals(dest)){
			links.add(new Link(hosts.get(it),switches.get(it2)));
			hosts.get(it).addLink();
			switches.get(it2).addLink();
		}
		else if(nHost > it2 && nSwitch > it && hosts.get(it2).getId().equals(dest) && switches.get(it).getId().equals(src)){
			links.add(new Link(switches.get(it),hosts.get(it2)));
			hosts.get(it2).addLink();
			switches.get(it).addLink();
		}
		else{
			links.add(new Link(switches.get(it),switches.get(it2)));
			switches.get(it).addLink();
			switches.get(it2).addLink();
		}
	}
	
	public boolean checkLinks(String src, String dest){
		for(Link aux : links){
			if(aux.getSrcId().equals(dest) && aux.getDestId().equals(src))
				return true;
		}
		return false;
	}
	
	public void clearTopology(){
		for(Switch auxS : switches){
			if(auxS.getnLink() == 1){
				for(int i = 0; i < links.size(); ++i){
					if(links.get(i).getDestId().equals(auxS.getId()) || links.get(i).getSrcId().equals(auxS.getId()))
						links.remove(i);
				}
			}
		}
	}
	
	public void printSwitch(){
		for(Switch aux : switches){
			System.out.println("Switch: " + aux.getId() + " numero de links: " + aux.getnLink());
		}
	}
	
	public void printHost(){
		for(Host aux : hosts)
			System.out.println("Host: " + aux.getId() + " numero de links: " + aux.getnLink());
	}
	
	public void printLink(){
		for(Link aux : links)
			System.out.println("Source: " + aux.getSrcId() + " Destination: " + aux.getDestId());
	}
}
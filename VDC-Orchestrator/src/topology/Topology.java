package topology;

import java.util.ArrayList;
import java.util.List;

import api.nova.Host;

public class Topology {

	List<Host> hosts = new ArrayList<Host>();
	List<Switch> switches = new ArrayList<Switch>();
	List<Link> links = new ArrayList<Link>();
	
	/**
	 * Add a host to the topology
	 * @param host - Host object to add
	 */
	public void addHost(Host host){
		hosts.add(host);
	}
	
	/**
	 * Check if the topology contains a Node
	 * @param id - The node ID
	 * @return
	 */
	public boolean containsHost(String id){
		boolean found = false;
		for(Host aux : hosts){
			found = id == aux.getMac();
			if(found)
				break;
		}
		return found;
	}
	
	/**
	 * Add a switch to the topology
	 * @param s - Switch object to add
	 */
	public void addSwitch(Switch s){
		switches.add(s);
	}
	
	/**
	 * Add a new link to the topology represented by a source and destination
	 * @param src - node ID representing a source
	 * @param it - list position of a source node
	 * @param dest - node ID representing a destination
	 * @param it2 - list position of the destination node
	 */
	public void addLink(String src, Integer it, String dest, Integer it2){
		int nHost = hosts.size();
		int nSwitch = switches.size();
		if(nHost > it && nSwitch > it2 && hosts.get(it).getId().equals(src) && switches.get(it2).getId().equals(dest)){
			links.add(new Link(hosts.get(it),switches.get(it2)));
			hosts.get(it).addLink_out(switches.get(it2));
			switches.get(it2).addLink_in(hosts.get(it));
		}
		else if(nHost > it2 && nSwitch > it && hosts.get(it2).getId().equals(dest) && switches.get(it).getId().equals(src)){
			links.add(new Link(switches.get(it),hosts.get(it2)));
			switches.get(it).addLink_out(hosts.get(it2));
			hosts.get(it2).addLink_in(switches.get(it));
		}
		else if(switches.get(it).equals(src) && switches.get(it2).equals(dest)){
			links.add(new Link(switches.get(it),switches.get(it2)));
			switches.get(it).addLink_out(switches.get(it2));
			switches.get(it2).addLink_in(switches.get(it));
		}
		else{
			links.add(new Link(switches.get(it2),switches.get(it)));
			switches.get(it2).addLink_out(switches.get(it));
			switches.get(it).addLink_in(switches.get(it2));
		}
	}
	
	/**
	 * Check if the links are already in the list
	 * @param src - node ID of the source
	 * @param dest - node ID of the destination
	 * @return
	 */
	public boolean checkLinks(String src, String dest){
		for(Link aux : links){
			if(aux.getSrcId().equals(dest) && aux.getDestId().equals(src))
				return true;
		}
		return false;
	}
	
	/**
	 * Clear topology in order to delete the virtual links
	 */
	public void clearTopology(){
		for(Switch auxS : switches){
			if(auxS.getNumLinks() == 2){
				for(int i = 0; i < links.size(); ++i){
					if(links.get(i).getDestId().equals(auxS.getId()) || links.get(i).getSrcId().equals(auxS.getId()))
						links.remove(i);
				}
			}
		}
	}
	
	/**
	 * Prints the switches list
	 */
	public void printSwitch(){
		for(Switch aux : switches){
			System.out.println("Switch: " + aux.getId() + " numero de links: " + aux.getNumLinks());
		}
	}
	
	/**
	 * Prints the hosts list
	 */
	public void printHost(){
		for(Host aux : hosts)
			System.out.println("Host: " + aux.getId() + " numero de links: " + aux.getNumLinks());
	}
	
	/**
	 * Prints the links list
	 */
	public void printLink(){
		for(Link aux : links)
			System.out.println("Source: " + aux.getSrcId() + " Destination: " + aux.getDestId());
	}
}
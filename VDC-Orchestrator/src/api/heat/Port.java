package api.heat;

import java.util.ArrayList;
import java.util.List;

public class Port {

	private Network_id network_id;
	private String name;
	private List<Subnet_id> fixed_ips = new ArrayList<Subnet_id>();
	
	public Port(Network_id network_id, String name, Network_id subnet_id){
		this.network_id = network_id;
		this.name = name;
		this.fixed_ips.add(new Subnet_id(subnet_id));
	}
	
	public String getName(){
		return name;
	}
}

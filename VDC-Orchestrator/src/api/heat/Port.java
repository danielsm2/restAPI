package api.heat;

import java.util.ArrayList;
import java.util.List;

public class Port {

	private String network_id;
	private String name;
	private String subnet_id;
	private List<Subnet_id> fixed_ips = new ArrayList<Subnet_id>();
	
	public Port(String network_id, String name, String subnet_id){
		this.network_id = network_id;
		this.name = name;
		this.fixed_ips.add(new Subnet_id(subnet_id));
	}
	
	public String getName(){
		return name;
	}
}

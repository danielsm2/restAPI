package api.heat;

public class Subnet_id {

	private Network_id subnet_id;
	
	public Subnet_id(Network_id subnet_id){
		this.setSubnet_id(subnet_id);
	}

	public Network_id getSubnet_id() {
		return subnet_id;
	}

	public void setSubnet_id(Network_id subnet_id) {
		this.subnet_id = subnet_id;
	}
}

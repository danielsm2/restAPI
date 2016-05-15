package api.heat;

public class Properties {

	private String name;
	private String image;
	private String flavor;
	private String availability_zone;
	private Network_id network_id;
	private String cidr;
	private String gateway_ip;
	private String enable_dhcp;
	private Networks networks;
	private Subnet_id fixed_ips;
	
	public Properties(String name){
		this.name = name;
	}
	public Properties(Network_id network_id, Subnet_id fixed_ips){
		this.network_id = network_id;
		this.fixed_ips = fixed_ips;
	}
	public Properties(String name, String image, String flavor, String availability_zone, String port){
		this.name = name;
		this.image = image;
		this.flavor = flavor;
		this.availability_zone = availability_zone;
		this.networks = new Networks(port);
	}
	
	public Properties(Network_id network_id, String name, String cidr, String gateway_ip, String enable_dhcp){
		this.network_id = network_id;
		this.name = name;
		this.cidr = cidr;
		this.gateway_ip = gateway_ip;
		this.enable_dhcp = enable_dhcp;
	}
	
	public String getName(){
		return name;
	}
}

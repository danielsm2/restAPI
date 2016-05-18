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
		this.setNetwork_id(network_id);
		this.setFixed_ips(fixed_ips);
	}
	public Properties(String name, String image, String flavor, String availability_zone, String port){
		this.name = name;
		this.setImage(image);
		this.setFlavor(flavor);
		this.setAvailability_zone(availability_zone);
		this.setNetworks(new Networks(port));
	}
	
	public Properties(Network_id network_id, String name, String cidr, String gateway_ip, String enable_dhcp){
		this.setNetwork_id(network_id);
		this.name = name;
		this.setCidr(cidr);
		this.setGateway_ip(gateway_ip);
		this.setEnable_dhcp(enable_dhcp);
	}
	
	public String getName(){
		return name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getFlavor() {
		return flavor;
	}
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}
	public String getAvailability_zone() {
		return availability_zone;
	}
	public void setAvailability_zone(String availability_zone) {
		this.availability_zone = availability_zone;
	}
	public Network_id getNetwork_id() {
		return network_id;
	}
	public void setNetwork_id(Network_id network_id) {
		this.network_id = network_id;
	}
	public String getCidr() {
		return cidr;
	}
	public void setCidr(String cidr) {
		this.cidr = cidr;
	}
	public String getGateway_ip() {
		return gateway_ip;
	}
	public void setGateway_ip(String gateway_ip) {
		this.gateway_ip = gateway_ip;
	}
	public String getEnable_dhcp() {
		return enable_dhcp;
	}
	public void setEnable_dhcp(String enable_dhcp) {
		this.enable_dhcp = enable_dhcp;
	}
	public Networks getNetworks() {
		return networks;
	}
	public void setNetworks(Networks networks) {
		this.networks = networks;
	}
	public Subnet_id getFixed_ips() {
		return fixed_ips;
	}
	public void setFixed_ips(Subnet_id fixed_ips) {
		this.fixed_ips = fixed_ips;
	}
}

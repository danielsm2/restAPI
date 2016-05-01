package api.heat;

public class Properties {

	private String name;
	private String image;
	private String flavor;
	private String availability_zone;
	private Network_id network_id;
	private String cidr;
	
	public Properties(String name){
		this.name = name;
	}
	
	public Properties(String name, String image, String flavor, String availability_zone){
		this.name = name;
		this.image = image;
		this.flavor = flavor;
		this.availability_zone = availability_zone;
	}
	
	public Properties(Network_id network_id, String name, String cidr){
		this.network_id = network_id;
		this.name = name;
		this.cidr = cidr;
	}
	
	public String getName(){
		return name;
	}
}

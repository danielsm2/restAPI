package vdc;

public class VMS {


	private String label;
	private String flavorID;
	private String imageID;
	
	public VMS(String label, String flavor, String image){
		this.label = label;
		this.flavorID = flavor;
		this.imageID = image;
	}
	
	/*public String getInfoMachine(){
		Iterator<String> it = VMS.keySet().iterator();
		String result = "";
		while(it.hasNext()){
			result = result + "\n" + it.next() +" : " +VMS.get(it.next());
		}
		return result;
	}*/
	
	public void printInfo(){
		System.out.println("label : " + label + " flavor : " + flavorID + " image : " + imageID);
	}
	
	public String getInfo(){
		return label + "','" + flavorID + "','" + imageID;
	}
}

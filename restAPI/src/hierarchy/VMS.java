package hierarchy;

public class VMS {


	private String vnode;
	private String ram;
	private String flavour;
	
	public VMS(String vnode, String ram, String flavour){
		this.vnode = vnode;
		this.ram = ram;
		this.flavour = flavour;
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
		System.out.println("vnode : " + vnode + " ram : " + ram + " flavour : " + flavour);
	}
}

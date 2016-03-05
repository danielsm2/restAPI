package hierarchy;

import java.util.ArrayList;
import java.util.List;

public class Hierarchy {

	private int tenant_id;
	private List<vNodes> vnodes= new ArrayList<vNodes>();
	private List<VMS> vms = new ArrayList<VMS>();
	private List<vLinks> vlinks = new ArrayList<vLinks>();
	
	public void addTenant(int tenant_id){
		this.tenant_id = tenant_id;
	}
	
	public void addNodes(vNodes vnodes){
		this.vnodes.add(vnodes);
	}
	
	public void addMachines(VMS vms){
		this.vms.add(vms);
	}
	
	public void addLinks(vLinks vlinks){
		this.vlinks.add(vlinks);
	}
	
	public void printInfo(){
		System.out.println("tenant_id : " + tenant_id);
		for(vNodes vn : vnodes){
			System.out.println("vnodes : " );
			vn.printInfo();
		}
		
		for(VMS vm : vms){
			System.out.println("vms : ");
			vm.printInfo();
		}
		
		for(vLinks vl : vlinks){
			System.out.println("vlinks : ");
			vl.printInfo();
		}
	}
}

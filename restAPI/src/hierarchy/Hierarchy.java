package hierarchy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Hierarchy {

	private String tenant_id;
	private List<vNodes> vnodes= new ArrayList<vNodes>();
	private List<VMS> vms = new ArrayList<VMS>();
	private List<vLinks> vlinks = new ArrayList<vLinks>();
	
	public void addTenant(String tenant_id){
		this.tenant_id = tenant_id;
	}
	
	public void addNodes(List<vNodes> vnodes){
		for(vNodes infoNode : vnodes){
			this.vnodes.add(infoNode);
		}
	}
	
	public void addMachines(List<VMS> vms){
		for(VMS infoMachine : vms){
			this.vms.add(infoMachine);
		}
	}
	
	public void addLinks(List<vLinks> vlinks){
		for(vLinks infoLinks : vlinks){
			this.vlinks.add(infoLinks);
		}
	}
}

package vdc;

import java.util.ArrayList;
import java.util.List;

public class VDC {

	private String tenantID;
	private List<vNodes> vnodes= new ArrayList<vNodes>();
	private List<vLinks> vlinks = new ArrayList<vLinks>();
	
	public void addTenant(String tenant_id){
		this.tenantID = tenant_id;
	}
	
	public void addNodes(vNodes vnodes){
		this.vnodes.add(vnodes);
	}
	
	public void addLinks(vLinks vlinks){
		this.vlinks.add(vlinks);
	}
	
	public int getNumElemVnode(){
		return vnodes.size();
	}
	
	public int getNumElemVlink(){
		return vlinks.size();
	}

	public String getInfoVdc(){
		return "INSERT INTO vdc VALUES ('" + tenantID + "')";
	}
	
	public String getInfoVnode(int i){
		String result = vnodes.get(i).getInfo();
		return "INSERT INTO vnode VALUES ('" + result + "','" + tenantID + "')"; 
	}
	
	public String getInfoVlink(int i){
		return "INSERT INTO vlink VALUES ('" + vlinks.get(i).getInfo() + "')";
	}
	
	public void addInfoVm(int i){
		vnodes.get(i).addInfoVm();
	}
	
	public void printInfo(){
		System.out.println("tenant_id : " + tenantID);
		System.out.println("vnodes : " );
		for(vNodes vn : vnodes){
			vn.printInfo();
		}
		
		System.out.println("vlinks : ");
		for(vLinks vl : vlinks){
			vl.printInfo();
		}
	}
}

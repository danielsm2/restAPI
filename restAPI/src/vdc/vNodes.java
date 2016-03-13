package vdc;

import java.util.ArrayList;
import java.util.List;

import db.DataBase;

public class vNodes {
	

	private String id;
	private String label;
	private List<VMS> vms = new ArrayList<VMS>();
	
	public vNodes(String id, String label){
		this.id = id;
		this.label = label;
	}
	
	/*public String getInfoLinks(){
		Iterator<String> it = vNodes.keySet().iterator();
		String result = "";
		while(it.hasNext()){
			result = result + "\n" + it.next() + " : " + vNodes.get(it.next());  
		}
		return result;
	}*/
	
	public void printInfo(){
		System.out.println("id : " + id + " label : " + label);
		System.out.println("vms : " );
		for(VMS v : vms){
			v.printInfo();
		}
	}

	public String getInfo(){
		return id + "','" + label;
	}
	
	public void addInfoVm(){
		String statement;
		DataBase db = DataBase.getInstance();
		for(VMS aux : vms){
			statement = "INSERT INTO vm VALUES ('" + aux.getInfo() + "','" + id + "')";
			db.addRow(statement);
		}
	}
}

package jsonParser;

import com.google.gson.Gson;

import vdc.VDC;

public class JsonParser {
	
	/*public String toJson(){
		Gson gson = new Gson();
		Persona person = new Persona("daniel", 23, "barcelona");
		String json = gson.toJson(person);
		System.out.println(json);
		return json;
	}*/
	
	public String toJson(String s){
		Gson gson = new Gson();
		String json = gson.toJson(s,new VDC().getClass());
		System.out.println(json);
		return json;
	}
	
	public VDC fromJson(String json){
		Gson gson = new Gson();
		VDC vdc = (VDC) gson.fromJson(json, new VDC().getClass());
		vdc.printInfo();
		return vdc;
		
	}
	
	/*public String HtoJson(){
		Gson gson = new Gson();
		VDC h = new VDC();
		h.addTenant(11);
		
		vNodes nodeInfo = new vNodes("vnode1","uuid");
		h.addNodes(nodeInfo);
		
		VMS machInfo = new VMS("vnode1", "4GB", "flavour1");
		h.addMachines(machInfo);
		machInfo = new VMS("vnode2", "8GB", "flavour2");
		h.addMachines(machInfo);
		
		vLinks linksInfo = new vLinks("vid1", "vnode1", "id1", "bandwith1");
		h.addLinks(linksInfo);
		
		String json = gson.toJson(h,VDC.class);
		System.out.println(json);
		return json;
	}*/
	
	public void HfromJson(String json){
		Gson gson = new Gson();
		VDC info = (VDC) gson.fromJson(json, new VDC().getClass());
		info.printInfo();
	}
}

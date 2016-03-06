package jsonParser;

import java.util.List;

import com.google.gson.Gson;

import hierarchy.Hierarchy;
import hierarchy.VMS;
import hierarchy.vLinks;
import hierarchy.vNodes;
import person.Persona;

public class JsonParser {

	List<Hierarchy> hierarchy;
	
	public String toJson(){
		Gson gson = new Gson();
		Persona person = new Persona("daniel", 23, "barcelona");
		String json = gson.toJson(person);
		System.out.println(json);
		return json;
	}
	
	public String toJson(String s){
		Gson gson = new Gson();
		Persona person = new Persona("daniel", 23, "barcelona");
		String json = gson.toJson(person);
		System.out.println(json);
		return json;
	}
	
	public Persona fromJson(String json){
		Gson gson = new Gson();
		Persona info = (Persona) gson.fromJson(json, new Persona().getClass());
		info.printInfo();
		return info;
		
	}
	
	public String HtoJson(){
		Gson gson = new Gson();
		Hierarchy h = new Hierarchy();
		h.addTenant(11);
		
		vNodes nodeInfo = new vNodes("vnode1","uuid");
		h.addNodes(nodeInfo);
		
		VMS machInfo = new VMS("vnode1", "4GB", "flavour1");
		h.addMachines(machInfo);
		machInfo = new VMS("vnode2", "8GB", "flavour2");
		h.addMachines(machInfo);
		
		vLinks linksInfo = new vLinks("vid1", "vnode1", "id1", "bandwith1");
		h.addLinks(linksInfo);
		
		String json = gson.toJson(h,Hierarchy.class);
		System.out.println(json);
		return json;
	}
	
	public void HfromJson(String json){
		Gson gson = new Gson();
		Hierarchy info = (Hierarchy) gson.fromJson(json, new Hierarchy().getClass());
		info.printInfo();
	}
}

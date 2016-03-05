package hierarchy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class vNodes {
	

	private String label;
	private String id;
	
	public vNodes(String label, String id){
		this.label = label;
		this.id = id;
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
		System.out.println("label : " + label + " id : " + id);
	}
}

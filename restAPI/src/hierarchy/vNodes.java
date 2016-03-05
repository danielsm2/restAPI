package hierarchy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class vNodes {
	
	private Map<String, String> vNodes = new HashMap<String,String>(200);

	public vNodes(Map<String, String> vNodes){
		this.vNodes = vNodes;
	}
	
	public void addNodes(String key, String value){
		vNodes.put(key, value);
	}
	public String getInfoLinks(){
		Iterator<String> it = vNodes.keySet().iterator();
		String result = "";
		while(it.hasNext()){
			result = result + "\n" + it.next() + " : " + vNodes.get(it.next());  
		}
		return result;
	}
}

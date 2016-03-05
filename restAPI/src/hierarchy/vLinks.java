package hierarchy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class vLinks {

	private Map<String, String> vLinks = new HashMap<String,String>(200);

	public vLinks(Map<String, String> vLinks){
		this.vLinks = vLinks;
	}
	
	public void addLinks(String key, String value){
		vLinks.put(key, value);
	}
	public String getInfoLinks(){
		Iterator<String> it = vLinks.keySet().iterator();
		String result = "";
		while(it.hasNext()){
			result = result + "\n" + it.next() + " : " + vLinks.get(it.next());  
		}
		return result;
	}
	
}

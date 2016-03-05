package hierarchy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VMS {

	private Map<String, String> VMS = new HashMap<String,String>(200);

	public VMS(Map<String, String> VMS){
		this.VMS = VMS;
	}
	
	public void addMachine(String key, String value){
		VMS.put(key, value);
	}
	public String getInfoMachine(){
		Iterator<String> it = VMS.keySet().iterator();
		String result = "";
		while(it.hasNext()){
			result = result + "\n" + it.next() +" : " +VMS.get(it.next());
		}
		return result;
	}
}

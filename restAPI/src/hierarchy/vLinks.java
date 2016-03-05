package hierarchy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class vLinks {

	private String source;
	private String destination;
	private String id;
	private String bandwith;
	
	public vLinks(String source, String destination, String id, String bandwith){
		this.source = source;
		this.destination = destination;
		this.id = id;
		this.bandwith = bandwith;
	}
	/*public String getInfoLinks(){
		Iterator<String> it = vLinks.keySet().iterator();
		String result = "";
		while(it.hasNext()){
			result = result + "\n" + it.next() + " : " + vLinks.get(it.next());  
		}
		return result;
	}*/
	
	public void printInfo(){
		System.out.println("source : " + source + " destination : " + destination + " id : "
				+ id + " bandwith : " + bandwith);
	}
}

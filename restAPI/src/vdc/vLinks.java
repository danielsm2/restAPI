package vdc;

public class vLinks {

	private String id;
	private String bandwith;
	private String bitrate;
	private String to;
	private String from;
	
	public vLinks(String id, String bandwith, String bitrate, String source, String destination){
		this.id = id;
		this.bandwith = bandwith;
		this.bitrate = bitrate;
		this.to = destination;
		this.from = source;
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
		System.out.println("to : " + to + " from : " + from + " id : "
				+ id + " bandwith : " + bandwith);
	}
	
	public String getInfo(){
		return id + "','" + bandwith + "','" + bitrate + "','" + to + "','" + from;
	}
}

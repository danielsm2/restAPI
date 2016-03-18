package vdc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class vLinks{

	private String id;
	private String bandwith;
	private String to;
	private String from;
	
	public vLinks(String id, String bandwith, String source, String destination){
		this.id = id;
		this.bandwith = bandwith;
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
	public String getId(){
		return id;
	}
	
	public String getBandwith(){
		return bandwith;
	}
	
	public String getDestination(){
		return to;
	}
	
	public String getSource(){
		return from;
	}
	
	public void printInfo(){
		System.out.println("to : " + to + " from : " + from + " id : "
				+ id + " bandwith : " + bandwith);
	}
	
	public String getInfo(boolean insert){
		String insertVlink = id + "','" + bandwith + "','" + to + "','" + from;
		String updateVlink = "bandwith=" + bandwith + ",to=" + to + ",from=" + from;
		if(insert)
			return insertVlink;
		return updateVlink;
	}
	
	public ErrorCheck check_vlink() {
		if(id.isEmpty() || bandwith.isEmpty() || to.isEmpty() || from.isEmpty()){
			return ErrorCheck.VLINK_NOT_COMPLETED;
		}
		return ErrorCheck.ALL_OK;
	}
	
	public boolean checkRow(ResultSet rs){
		try{
			while(rs.next()){
				if(rs.getString("id").equals(id))
					return false;
			}
		}catch(SQLException e){
			System.err.println(e);
		}
		return true;
	}
}

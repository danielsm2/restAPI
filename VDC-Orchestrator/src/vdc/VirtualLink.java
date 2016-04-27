package vdc;

import java.sql.ResultSet;
import java.sql.SQLException;

/** The VirtualLink class represents a virtual link requested within a VDC instance. It states the desired bit-rate between a (source, destination) pair of virtual nodes.
 * 
 * @author Albert Pagès, Dani Sanchez, Universitat Politècnica de Catalunya (UPC), Barcelona, Spain
 * @version v4.0 March 01, 2016
 * 
 */

public class VirtualLink{

	/** The ID of the virtual link */
	private String id;
	
	private String bandwith;
	
	/** The destination virtual node of the virtual link. */
	private String to;
	
	/** The source virtual node of the virtual link. */
	private String from;
	
	public VirtualLink(String id, String bandwith, String destination, String source){
		this.id = id;
		this.bandwith = bandwith;
		this.to = destination;
		this.from = source;
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public void setSource(String from){
		this.from = from;
	}
	
	public void setDestination(String to){
		this.to = to;
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
		if(id.isEmpty() || id == null || bandwith.isEmpty() || bandwith == null ||
				to.isEmpty() || to == null || from.isEmpty() || from == null){
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

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
	
	/**
	 * Creates a new virtual link instance
	 * @param id - the ID of the virtual link
	 * @param bandwith - the bandwith of the virtual link
	 * @param destination - the virtual node destination of the virtual link
	 * @param source - the virtual node source of the virtual link
	 */
	public VirtualLink(String id, String bandwith, String destination, String source){
		this.id = id;
		this.bandwith = bandwith;
		this.to = destination;
		this.from = source;
	}
	
	/**
	 * Get the virtual link ID
	 * @return
	 */
	public String getID(){
		return id;
	}
	
	/**
	 * Set the virtual link ID identified by id
	 * @param id - the new ID of the virtual link
	 */
	public void setID(String id){
		this.id = id;
	}
	
	/**
	 * Set the virtual link source reference of a virtual node
	 * @param from - id of the virtual node source
	 */
	public void setSource(String from){
		this.from = from;
	}
	
	/**
	 * Set the virtual link destination reference of a virtual node
	 * @param to - id of the virtual node destination
	 */
	public void setDestination(String to){
		this.to = to;
	}
	
	/**
	 * Get the bandwith of the virtual link
	 * @return
	 */
	public String getBandwith(){
		return bandwith;
	}
	
	/**
	 * Get the virtual node destination ID of the virtual link
	 * @return
	 */
	public String getDestination(){
		return to;
	}
	
	/**
	 * Get the virtual node source ID of the virtual link
	 * @return
	 */
	public String getSource(){
		return from;
	}
	
	/**
	 * Print the virtual link information
	 */
	public void printInfo(){
		System.out.println("to : " + to + " from : " + from + " id : "
				+ id + " bandwith : " + bandwith);
	}
	
	/**
	 * Get the proper virtual link information depending on action
	 * @param action - a boolean that is true represents an insert else an update against DB
	 * @return
	 */
	public String getInfo(boolean action){
		String insertVlink = id + "','" + bandwith + "','" + to + "','" + from;
		String updateVlink = "bandwith=" + bandwith + ",to=" + to + ",from=" + from;
		if(action)
			return insertVlink;
		return updateVlink;
	}
	
	/**
	 * Check if the virtual link information given is wrong
	 * @return
	 */
	public ErrorCheck checkVLink() {
		if(id.isEmpty() || id == null || bandwith.isEmpty() || bandwith == null ||
				to.isEmpty() || to == null || from.isEmpty() || from == null){
			return ErrorCheck.VLINK_NOT_COMPLETED;
		}
		return ErrorCheck.ALL_OK;
	}
	
	/**
	 * Check if the DB row given is the same as this current virtual link  
	 * @param rs - the result of the select
	 * @return
	 */
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

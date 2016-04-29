package vdc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;

/** The VirtualNode class represents a virtual node requested within a VDC instance. It is composed by a collection of Virtual Machines (VMs)
 * that are collocated onto the same physical rack at the DC infrastructure.
 * 
 * @author Albert Pagès, Dani Sanchez, Universitat Politècnica de Catalunya (UPC), Barcelona, Spain
 * @version v4.0 March 01, 2016
 * 
 */

public class VirtualNode {
	
	/** The ID of the virtual node. */
	private String id;
	
	/** A string representing the label (i.e. name) given to the virtual node. */
	private String label;
	
	/**  An ArryList representing the collection of virtual machines (VMs) requested within the virtual node. */
	private List<VirtualMachine> vms = new ArrayList<VirtualMachine>();
	
	/**
	 * Creates a new virtual node instance
	 * @param id - the ID of the virtual node
	 * @param label - the label of the virtual node
	 */
	public VirtualNode(String id, String label){
		this.id = id;
		this.label = label;
	}
	
	/**
	 * Get the virtual node ID
	 * @return
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Set the ID of the virtual node
	 * @param id - the new ID of the virtual node
	 */
	public void setID(String id){
		this.id = id;
	}
	
	/**
	 * Add a new virtual machine instance in the list vms
	 * @param vm - the new virtual machine instance to add
	 */
	public void addVirtualMachine(VirtualMachine vm){
		vms.add(vm);
	}
	
	/**
	 * Print the virtual node information
	 */
	public void printInfo(){
		System.out.println("id : " + id + " label : " + label);
		System.out.println("vms : " );
		for(VirtualMachine v : vms){
			v.printInfo();
		}
	}

	/**
	 * Get the virtual node information in order to generate a DB statement
	 * @param action - a boolean that is true represents an insert else an update against DB 
	 * @return
	 */
	public String getInfo(boolean action){
		String updateVnode = label;
		String insertVnode = id + "','" + label;
		if (action)
			return insertVnode;
		return updateVnode;
	}
	
	/**
	 * Get the label field of the virtual node
	 * @return
	 */
	public String getLabel(){
		return label;
	}
	
	/**
	 * Get the number of elements of the virtual machines related to this virtual node
	 * @return
	 */
	public int getSizeVirtualMachine(){
		return vms.size();
	}
	
	/**
	 * Get a virtual machine instance identified by a list position
	 * @param i - the list position
	 * @return
	 */
	public VirtualMachine getVirtualMachine(int i){
		return vms.get(i);
	}
	
	/**
	 * Performs an insert or update against the virtual machine table
	 * @throws SQLException
	 */
	public void updateVM() throws SQLException{
		DataBase db = DataBase.getInstance();
		ResultSet rs;
		for(VirtualMachine aux : vms){
			PreparedStatement ps = db.prepareStatement("SELECT id FROM vm WHERE id = ?");
			ps.setString(1, aux.getId());
			System.out.println(aux.getId());
			System.out.println(this.id);
			rs = db.queryDB(ps); 
			if(aux.checkRowDBVM(rs)){
			    String id = aux.getId();
				String label = aux.getLabel();
				String flavorID = aux.getFlavorID();
				String flavorName = aux.getFlavorName();
				String image = aux.getImage();
				db.newEntryDB("INSERT INTO vm VALUES ('" + id + "','" + label + "','" + flavorID + "','" + flavorName + "','" + image + "','" + this.id + "')");
			}
			else{
				ps = db.prepareStatement("UPDATE vm SET label=?,flavorID=?,flavorName=?,imageID=? WHERE id=?");
				ps.setString(1, aux.getLabel());
				ps.setString(2, aux.getFlavorID());
				ps.setString(3, aux.getFlavorName());
				ps.setString(4, aux.getImage());
				ps.setString(5, aux.getId());
				ps.executeUpdate();
			}
		}
	}
	
	/**
	 * Check the virtual node defined in order to find incomplete fields
	 * @return
	 */
	public ErrorCheck checkVNode(){
		if(id.isEmpty() || label.isEmpty() || id == null || label == null)
			return ErrorCheck.VNODE_NOT_COMPLETED;
		ErrorCheck ec;
		for(VirtualMachine aux : vms){
			ec = aux.checkVM();
			if(ec.equals(ErrorCheck.VM_NOT_COMPLETED)){
				return ec;
			}
		}
		return ErrorCheck.ALL_OK;
	}
	
	/**
	 * Check if exist an equal virtual node at virtual node table
	 * @param rs - the result of the select
	 * @return
	 */
	public boolean checkRowDBVNode(ResultSet rs){
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
	
	/**
	 * Set the ID of each virtual machine in order to get an unique virtual machine ID 
	 */
	public void assignIDtoVM(){
		int count = 0;
		for(VirtualMachine aux : vms)
			aux.setId(id + Integer.toString(count++));
	}
}

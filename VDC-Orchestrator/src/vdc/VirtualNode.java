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
	
	public VirtualNode(String id, String label){
		this.id = id;
		this.label = label;
	}
	
	public void addVM(VirtualMachine vm){
		vms.add(vm);
	}
	
	public void printInfo(){
		System.out.println("id : " + id + " label : " + label);
		System.out.println("vms : " );
		for(VirtualMachine v : vms){
			v.printInfo();
		}
	}

	public String getInfo(boolean insert){
		String updateVnode = label;
		String insertVnode = id + "','" + label;
		if (insert)
			return insertVnode;
		return updateVnode;
	}
	
	public String getLabel(){
		return label;
	}
	
	public void updateVM() throws SQLException{
		DataBase db = DataBase.getInstance();
		ResultSet rs;
		for(VirtualMachine aux : vms){
			PreparedStatement ps = db.prepareStatement("SELECT id FROM vm WHERE id = ?");
			ps.setString(1, aux.getId());
			rs = db.queryDB(ps); 
			if(aux.checkRow(rs)){
			    String id = aux.getId();
				String label = aux.getLabel();
				String flavor = aux.getFlavor();
				String image = aux.getImage();
				db.newEntryDB("INSERT INTO vm VALUES ('" + id + "','" + label + "','" + flavor + "','" + image + "','" + this.id + "')");
				System.out.println("insert vm");
			}
			else{
				ps = db.prepareStatement("UPDATE vm SET label=?,flavorID=?,imageID=? WHERE id=?");
				ps.setString(1, aux.getLabel());
				ps.setString(2, aux.getFlavor());
				ps.setString(3, aux.getImage());
				ps.setString(4, aux.getId());
				ps.executeUpdate();
				System.out.println("update vm");
			}
		}
	}
	
	public String getId(){
		return id;
	}
	
	public ErrorCheck check_vnode(){
		if(id.isEmpty() || label.isEmpty() || id == null || label == null)
			return ErrorCheck.VNODE_NOT_COMPLETED;
		ErrorCheck ec;
		for(VirtualMachine aux : vms){
			ec = aux.check_vm();
			if(ec.equals(ErrorCheck.VM_NOT_COMPLETED)){
				return ec;
			}
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
	public void assignIDtoVM(){
		int count = 0;
		for(VirtualMachine aux : vms)
			aux.setId(id + Integer.toString(count++));
	}
}
